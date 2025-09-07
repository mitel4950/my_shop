package alien.marshmallow.shared.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

  public BadRequestException(Object object, String message) {
    super(object, message, HttpStatus.BAD_REQUEST);
  }
}
