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

  //TODO: добавить авто-логирование в перехвадчик запросов и создать анатацию, которая будет скрывать значение поля в логах

  @NotNull(message = "Login is required")
  private String login;

  @Sensitive
  @NotNull(message = "Password is required")
  private String password;

}
