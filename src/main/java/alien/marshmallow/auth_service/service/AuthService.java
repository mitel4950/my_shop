package alien.marshmallow.auth_service.service;

import alien.marshmallow.auth_service.config.JwtProperties;
import alien.marshmallow.auth_service.domain.dto.LoginRequest;
import alien.marshmallow.auth_service.domain.dto.SignUpRequest;
import alien.marshmallow.auth_service.domain.dto.TokenResponse;
import alien.marshmallow.auth_service.domain.entity.UserAuthEntity;
import alien.marshmallow.auth_service.domain.enums.UserRole;
import alien.marshmallow.shared.exception.BadRequestException;
import alien.marshmallow.auth_service.exception.RefreshTokenException;
import alien.marshmallow.auth_service.mapper.UserMapper;
import alien.marshmallow.auth_service.repository.postgres.UserAuthRepository;
import alien.marshmallow.auth_service.repository.redis.RedisAuthTokenRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserAuthRepository repository;
  private final UserMapper mapper;
  private final RedisAuthTokenRepository redis;

  private final JwtProperties jwtProperties;

  @Transactional
  public void signup(SignUpRequest req) {
    validateSignUpRequest(req);
    UserAuthEntity entity = mapper.toEntity(req);
    entity.setRole(UserRole.CLIENT);
    repository.save(entity);
  }

  @Transactional(readOnly = true)
  public TokenResponse login(LoginRequest req) {
    UserAuthEntity user = repository.findByLoginAndPassword(req.getLogin(), req.getPassword())
        .orElseThrow(() -> new BadRequestException("login, password",
                                                   "incorrect login and/or password"));

    String accessToken = generateAccessToken(user);
    String refreshToken = generateRefreshToken(user);
    redis.saveRefreshToken(user.getLogin(), refreshToken, jwtProperties.getRefreshTtl());

    return new TokenResponse(accessToken, refreshToken, jwtProperties.getAccessTtl());
  }

  @Transactional(readOnly = true)
  public TokenResponse refresh(String refreshToken) {
    validateRefreshToken(refreshToken);
    UserAuthEntity user = getUserByRefreshToken(refreshToken);

    String newAccessToken = generateAccessToken(user);
    String newRefreshToken = generateRefreshToken(user);

    redis.saveRefreshToken(user.getLogin(), newRefreshToken, jwtProperties.getRefreshTtl());
    redis.deleteRefreshToken(refreshToken);

    return new TokenResponse(newAccessToken, newRefreshToken, jwtProperties.getAccessTtl());
  }

  @Transactional(readOnly = true)
  public void logout(String refreshToken) {
    validateRefreshToken(refreshToken);
    getUserByRefreshToken(refreshToken);
    redis.deleteRefreshToken(refreshToken);
  }


  private String generateAccessToken(UserAuthEntity user) {
    return generateToken(user,
                         jwtProperties.getAccessTtl(),
                         "access",
                         user.getRole().name());
  }

  private String generateRefreshToken(UserAuthEntity user) {
    return generateToken(user, jwtProperties.getRefreshTtl(), "refresh", null);
  }

  private String generateToken(UserAuthEntity user,
                               long expirationTime,
                               String tokenType,
                               Object roleValue) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expirationTime);

    try {
      Builder calimsBuilder = new Builder()
          .subject(user.getLogin())
          .issuer(jwtProperties.getIssuer())
          .audience(jwtProperties.getAudience())
          .issueTime(Date.from(now))
          .expirationTime(Date.from(exp))
          .jwtID(UUID.randomUUID().toString())
          .claim("type", tokenType);

      if (roleValue != null) {
        calimsBuilder.claim("role", roleValue);
      }

      JWTClaimsSet claims = calimsBuilder.build();

      JWSSigner signer = new MACSigner(jwtProperties.getSecret().getBytes());
      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (Exception e) {
      throw new RuntimeException("JWT generation error", e);
    }
  }


  private void validateSignUpRequest(SignUpRequest req) {
    if (repository.existsByLogin(req.getLogin())) {
      throw new BadRequestException("login", "Login already exists");
    } else if (repository.existsByNickname(req.getNickname())) {
      throw new BadRequestException("nickname", "Nickname already exists");
    }
  }

  private void validateRefreshToken(String refreshToken) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(refreshToken);

      JWSVerifier verifier = new MACVerifier(jwtProperties.getSecret().getBytes());
      if (!signedJWT.verify(verifier)) {
        throw new RefreshTokenException("Invalid token signature");
      }

      JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
      if (claims.getExpirationTime().before(new Date())) {
        throw new RefreshTokenException("Token expired");
      }

      String type = claims.getStringClaim("type");
      if (!"refresh".equals(type)) {
        throw new RefreshTokenException("Not a refresh token");
      }
    } catch (Exception e) {
      throw new RefreshTokenException("Invalid refresh token");
    }
  }


  private UserAuthEntity getUserByRefreshToken(String refreshToken) {
    return redis.getLoginByRefreshToken(refreshToken)
        .flatMap(repository::findByLogin)
        .orElseThrow(() -> new RefreshTokenException("Invalid or expired refresh token"));
  }
}
