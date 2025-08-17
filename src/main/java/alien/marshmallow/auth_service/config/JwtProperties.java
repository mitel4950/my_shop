package alien.marshmallow.auth_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtProperties {

  @Value("${app.jwt.issuer}")
  private String issuer;

  @Value("${app.jwt.audience}")
  private String audience;

  @Value("${app.jwt.access-ttl-seconds}")
  private long accessTtl;

  @Value("${app.jwt.refresh-ttl-seconds}")
  private long refreshTtl;

  @Value("${app.jwt.secret}")
  private String secret;
}
