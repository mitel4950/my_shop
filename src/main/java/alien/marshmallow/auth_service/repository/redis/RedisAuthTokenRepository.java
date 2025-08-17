package alien.marshmallow.auth_service.repository.redis;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisAuthTokenRepository {

  private static final String LOGIN_TOKENS = "login:";  // login:[token1, token2]
  private static final String TOKEN_LOGIN = "token:";   // token:login

  private final StringRedisTemplate redis;

  @Value("${app.jwt.max-sessions-per-user}")
  private int maxSessionsPerUser;

  public void saveRefreshToken(String login, String token, long ttlSeconds) {
    String loginKey = LOGIN_TOKENS + login;
    String tokenKey = TOKEN_LOGIN + token;

    redis.opsForValue().set(tokenKey, login, Duration.ofSeconds(ttlSeconds));
    redis.opsForList().rightPush(loginKey, token);
    redis.expire(loginKey, Duration.ofSeconds(ttlSeconds));

    Long size = redis.opsForList().size(loginKey);
    if (size != null && size > maxSessionsPerUser) {
      String oldest = redis.opsForList().leftPop(loginKey);
      if (oldest != null) {
        redis.delete(TOKEN_LOGIN + oldest);
      }
    }
  }

  public Optional<String> getLoginByRefreshToken(String token) {
    return Optional.ofNullable(redis.opsForValue().get(TOKEN_LOGIN + token));
  }

  public void deleteRefreshToken(String token) {
    String login = redis.opsForValue().get(TOKEN_LOGIN + token);
    if (login != null) {
      String loginKey = LOGIN_TOKENS + login;
      redis.opsForList().remove(loginKey, 1, token);
    }
    redis.delete(TOKEN_LOGIN + token);
  }
}
