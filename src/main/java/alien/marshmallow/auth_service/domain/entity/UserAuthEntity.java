package alien.marshmallow.auth_service.domain.entity;

import alien.marshmallow.auth_service.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_auth")
public class UserAuthEntity {

  @Id
  @Column(name = "login", nullable = false, updatable = false)
  private String login;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "nickname", nullable = false, unique = true)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private UserRole role;
}
