package alien.marshmallow.auth_service.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  @NotNull(message = "Nickname is required")
  @Size(min = 1, max = 40, message = "Nickname must be 1-40 characters long")
  @Pattern(regexp = "^\\w+$", message = "Nickname can contain only letters, digits and underscore")
  private String nickname;

  @NotNull(message = "Login is required")
  @Size(min = 3, max = 50, message = "Login must be 3-50 characters long")
  @Pattern(regexp = "^\\S+$", message = "Login must not contain spaces")
  private String login;

  @NotNull(message = "Password is required")
  @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
  private String password;

}
