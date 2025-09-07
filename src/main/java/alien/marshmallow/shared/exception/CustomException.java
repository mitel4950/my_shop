package alien.marshmallow.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  @Getter
  private final String object;

  @Getter
  private final HttpStatus httpStatus;

  public CustomException(Object object, String message, HttpStatus httpStatus) {
    super(message);
    this.object = object == null ? null : object.toString();
    this.httpStatus = httpStatus;
  }
}
