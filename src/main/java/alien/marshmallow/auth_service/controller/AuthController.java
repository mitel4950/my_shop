package alien.marshmallow.auth_service.controller;

import alien.marshmallow.auth_service.annotations.SuccessResponse;
import alien.marshmallow.auth_service.domain.dto.LoginRequest;
import alien.marshmallow.auth_service.domain.dto.RefreshTokenRequest;
import alien.marshmallow.auth_service.domain.dto.SignUpRequest;
import alien.marshmallow.auth_service.domain.dto.TokenResponse;
import alien.marshmallow.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @PostMapping("/signup")
  @SuccessResponse(status = HttpStatus.CREATED, message = "User registered successfully")
  public void signup(@RequestBody @Valid SignUpRequest req) {
    service.signup(req);
  }

  @PostMapping("/login")
  public TokenResponse login(@RequestBody @Valid LoginRequest req) {
    return service.login(req);
  }

  @PostMapping("/refresh")
  public TokenResponse refresh(@RequestBody @Valid RefreshTokenRequest req) {
    return service.refresh(req.getRefreshToken());
  }

  @PostMapping("/logout")
  @SuccessResponse(status = HttpStatus.OK, message = "User logged out successfully")
  public void logout(@RequestBody @Valid RefreshTokenRequest req) {
    service.logout(req.getRefreshToken());
  }
}

