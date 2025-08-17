package alien.marshmallow.auth_service.domain.dto;

import alien.marshmallow.auth_service.annotations.Sensitive;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

  @NotNull
  @NotBlank
  @Sensitive
  private String refreshToken;

}
