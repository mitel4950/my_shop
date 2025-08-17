package alien.marshmallow.auth_service.domain.dto;

import alien.marshmallow.auth_service.annotations.Sensitive;
import lombok.Data;

@Data
public class TokenResponse {

  @Sensitive
  private String accessToken;

  @Sensitive
  private String refreshToken;

  private String tokenType;
  private long expiresIn; // in seconds

  public TokenResponse(String accessToken, String refreshToken, long expiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.tokenType = "Bearer";
  }
}
