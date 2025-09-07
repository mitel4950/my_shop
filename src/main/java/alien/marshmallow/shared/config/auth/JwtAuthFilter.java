package alien.marshmallow.shared.config.auth;

import alien.marshmallow.shared.domain.UserRole;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtProperties jwt;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    try {
      SignedJWT jwtToken = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(jwt.getSecret().getBytes());
      if (!jwtToken.verify(verifier)) {
        chain.doFilter(request, response);
        return;
      }

      JWTClaimsSet claims = jwtToken.getJWTClaimsSet();

      String type = claims.getStringClaim("type");
      if (type != null && !"access".equals(type)) {
        chain.doFilter(request, response);
        return;
      }

      Date exp = claims.getExpirationTime();
      if (exp == null || exp.before(new Date())) {
        chain.doFilter(request, response);
        return;
      }

      String login = claims.getSubject();
      String role = Optional.ofNullable(claims.getStringClaim("role"))
          .orElse(UserRole.CLIENT.toString());

      List<SimpleGrantedAuthority> authorities =
          List.of(new SimpleGrantedAuthority("ROLE_" + role));

      JwtUserAuthentication auth = new JwtUserAuthentication(login, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (ServletException | IOException e) {
      throw e;
    } catch (Exception ignore) {
    }

    chain.doFilter(request, response);
  }

  static final class JwtUserAuthentication extends AbstractAuthenticationToken {

    private final String principal;

    JwtUserAuthentication(String principal, Collection<SimpleGrantedAuthority> authorities) {
      super(authorities);
      this.principal = principal;
      setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
      return "";
    }

    @Override
    public Object getPrincipal() {
      return getName();
    }

    @Override
    public String getName() {
      return principal;
    }
  }
}
