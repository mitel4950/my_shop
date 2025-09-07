package alien.marshmallow.auth_service.exception;

import alien.marshmallow.shared.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RefreshTokenException extends CustomException {

  public RefreshTokenException(String message) {
    super("refreshToken", message, HttpStatus.UNAUTHORIZED);
  }
}
