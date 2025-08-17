package alien.marshmallow.auth_service.domain.dto;

import lombok.Data;

@Data
public class TokenResponse {

  private String accessToken;
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
