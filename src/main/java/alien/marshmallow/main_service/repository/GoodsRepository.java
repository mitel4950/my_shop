package alien.marshmallow.main_service.repository;

import alien.marshmallow.main_service.domain.entity.GoodsEntity;
import alien.marshmallow.shared.exception.BadRequestException;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsEntity, UUID> {

  default GoodsEntity getRequired(UUID id) {
    return findById(id).orElseThrow(
        () -> new BadRequestException(id, "Goods not found by id: " + id));
  }

}
