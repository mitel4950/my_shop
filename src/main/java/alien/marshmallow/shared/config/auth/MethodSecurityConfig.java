package alien.marshmallow.shared.config.auth;

import alien.marshmallow.shared.annotations.RequireAbility;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

  private final JwtProperties jwtProperties;

  @Bean
  public AuthorizationManagerBeforeMethodInterceptor requireRolesMethodInterceptor() {
    Pointcut classPointcut = new AnnotationMatchingPointcut(RequireAbility.class, true);
    Pointcut methodPointcut = new AnnotationMatchingPointcut(null, RequireAbility.class, true);
    Pointcut pointcut = new ComposablePointcut(classPointcut).union(methodPointcut);

    var manager = new RequireRolesAuthorizationManager();
    var interceptor = new AuthorizationManagerBeforeMethodInterceptor(pointcut, manager);
    interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() - 10);
    return interceptor;
  }

  @Bean
  public SecurityFilterChain api(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> {})
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(reg -> reg.anyRequest().permitAll())
        .addFilterBefore(new JwtAuthFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}
