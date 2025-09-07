package alien.marshmallow.main_service.repository;

import alien.marshmallow.main_service.domain.entity.GoodsEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsEntity, UUID> {

}
