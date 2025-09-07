package alien.marshmallow.shared.config.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class HttpLoggingAspect {

  private final ObjectMapper logMapper;

  public HttpLoggingAspect(ObjectMapper appMapper) {
    this.logMapper = appMapper.copy();
    this.logMapper.registerModule(new SensitiveMaskingModule());
  }

  @Around("within(@org.springframework.web.bind.annotation.RestController *)")
  public Object logHttp(ProceedingJoinPoint pjp) throws Throwable {
    HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String method = req.getMethod();
    String uri = req.getRequestURI();
    String qs = req.getQueryString() != null ? "?" + req.getQueryString() : "";
    String reqBody = buildBodyJson(pjp.getArgs());

    String principal = "ANONIM";

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null
        && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getName())) {
      principal = authentication.getName();
    }

    StringBuilder requestLog = new StringBuilder()
        .append(principal).append(" ")
        .append("Request: ")
        .append(method).append(" ")
        .append(method).append(qs);

    if (!reqBody.isBlank()) {
      requestLog.append("\nBODY:\n").append(reqBody);
    }
    log.info(requestLog.toString());

    Object result;
    int status = 200;
    try {
      result = pjp.proceed();
      status = resolveStatus(result, pjp);
    } catch (Throwable ex) {
      Integer exStatus = resolveExceptionStatus(ex);
      if (exStatus != null) {
        status = exStatus;
      }
      log.error("Response: {} {}{}\nSTATUS: {}\nERROR: {}",
                method, uri, qs, status, ex.getMessage());
      throw ex;
    }

    Object respBody = (result instanceof ResponseEntity<?> re) ? re.getBody() : result;
    String respJson = toJson(respBody);
    log.info("{} Response: {} {}{}\nSTATUS: {}\nBODY:\n{}", principal, method, uri, qs, status, respJson);

    return result;
  }

  private String buildBodyJson(Object[] args) {
    StringBuilder sb = new StringBuilder();
    for (Object a : args) {
      if (a == null
          || a instanceof String
          || a instanceof Number
          || a instanceof Boolean
          || a instanceof Enum<?>
          || a instanceof jakarta.servlet.ServletRequest
          || a instanceof jakarta.servlet.ServletResponse) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append("\n");
      }
      sb.append(toJson(a));
    }
    return sb.toString();
  }

  private String toJson(Object body) {
    try {
      if (body == null) {
        return "null";
      }
      return logMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
    } catch (Exception e) {
      return "{ \"_log_error\": \"" + e.getMessage() + "\" }";
    }
  }

  private int resolveStatus(Object result, ProceedingJoinPoint pjp) {
    if (result instanceof ResponseEntity<?> re) {
      return re.getStatusCode().value();
    }
    Method m = ((MethodSignature) pjp.getSignature()).getMethod();
    ResponseStatus rs = m.getAnnotation(ResponseStatus.class);
    return rs != null ? rs.code().value() : 200;
  }

  static Integer resolveExceptionStatus(Throwable ex) {
    ResponseStatus rs = ex.getClass().getAnnotation(ResponseStatus.class);
    return rs != null ? rs.code().value() : null;
  }
}

