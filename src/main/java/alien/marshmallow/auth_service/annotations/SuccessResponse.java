package alien.marshmallow.auth_service.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.HttpStatus;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SuccessResponse {

  HttpStatus status();

  String message();
}
