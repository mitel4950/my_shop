package alien.marshmallow.shared.config.auth;

import alien.marshmallow.shared.annotations.RequireAbility;
import alien.marshmallow.shared.domain.Ability;
import alien.marshmallow.shared.domain.UserRole;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class RequireRolesAuthorizationManager implements AuthorizationManager<MethodInvocation> {

  @Override
  public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation mi) {
    RequireAbility ann = findAnnotation(mi);
    if (ann == null) {
      return null;
    }

    Authentication auth = authentication.get();
    if (auth == null || !auth.isAuthenticated()) {
      return new AuthorizationDecision(false);
    }

    Set<Ability> authorities = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(v -> v.startsWith("ROLE_"))
        .map(v -> v.substring(5))
        .map(UserRole::fromString)
        .filter(Objects::nonNull)
        .map(UserRole::getAbilities)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());

    return new AuthorizationDecision(Arrays.stream(ann.value()).anyMatch(authorities::contains));
  }

  private RequireAbility findAnnotation(MethodInvocation mi) {
    Method m = mi.getMethod();
    RequireAbility onMethod = AnnotatedElementUtils.findMergedAnnotation(m, RequireAbility.class);
    if (onMethod != null) return onMethod;

    Class<?> targetClass = mi.getThis() != null ? mi.getThis().getClass() : m.getDeclaringClass();
    return AnnotatedElementUtils.findMergedAnnotation(targetClass, RequireAbility.class);
  }

}
