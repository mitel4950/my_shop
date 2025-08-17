package alien.marshmallow.auth_service.config;

import alien.marshmallow.auth_service.exception.CustomException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
        .collect(Collectors.toMap(
            error -> ((FieldError) error).getField(),
            DefaultMessageSourceResolvable::getDefaultMessage
        ));

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(CustomException ex) {

    Map<String, String> errors = new HashMap<>();
    errors.put(ex.getObject(), ex.getMessage());

    return new ResponseEntity<>(errors, ex.getHttpStatus());
  }

}
