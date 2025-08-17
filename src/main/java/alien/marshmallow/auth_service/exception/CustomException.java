package alien.marshmallow.auth_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  @Getter
  private final String object;

  @Getter
  private final HttpStatus httpStatus;

  public CustomException(String object, String message, HttpStatus httpStatus) {
    super(message);
    this.object = object;
    this.httpStatus = httpStatus;
  }
}
