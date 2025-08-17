package alien.marshmallow.auth_service.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends CustomException{

  public RefreshTokenException(String message) {
    super("refreshToken", message, HttpStatus.UNAUTHORIZED);
  }
}
