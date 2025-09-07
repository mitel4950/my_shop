package alien.marshmallow.shared.domain;

import java.util.Arrays;
import java.util.Set;
import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN(Ability.values()),
  MODERATOR(Ability.READ),
  SELLER(Ability.READ, Ability.GOODS_CRAD),
  CLIENT(Ability.READ);

  private final Set<Ability> abilities;


  UserRole(Ability... abilities) {
    this.abilities = Set.of(abilities);
  }

  public static UserRole fromString(String role) {
    return Arrays.stream(values())
        .filter(r -> r.name().equalsIgnoreCase(role))
        .findFirst()
        .orElse(null);
  }

}
