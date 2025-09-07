package alien.marshmallow.shared.config;

import alien.marshmallow.shared.annotations.SuccessResponse;
import alien.marshmallow.auth_service.domain.dto.MessageResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
                          Class<? extends HttpMessageConverter<?>> converterType) {
    return returnType.hasMethodAnnotation(SuccessResponse.class);
  }

  @Override
  public Object beforeBodyWrite(Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request,
                                ServerHttpResponse response) {
    SuccessResponse ann = returnType.getMethodAnnotation(SuccessResponse.class);
    if (ann != null) {
      response.setStatusCode(ann.status());
      return new MessageResponse(ann.message());
    }
    return body;
  }
}
