package alien.marshmallow.auth_service.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

  public BadRequestException(String object, String message) {
    super(object, message, HttpStatus.BAD_REQUEST);
  }
}
