package alien.marshmallow.auth_service.domain.dto;

import alien.marshmallow.auth_service.annotations.Sensitive;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotNull(message = "Login is required")
  private String login;

  @Sensitive
  @NotNull(message = "Password is required")
  private String password;

}
