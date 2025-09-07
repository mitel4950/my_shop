package alien.marshmallow.auth_service.repository;

import alien.marshmallow.auth_service.domain.entity.UserAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, String> {

  boolean existsByLogin(String login);

  boolean existsByNickname(String nickName);

  Optional<UserAuthEntity> findByLoginAndPassword(String login, String password);

  Optional<UserAuthEntity> findByLogin(String login);
}
