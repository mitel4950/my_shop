package alien.marshmallow.main_service.mapper;

import alien.marshmallow.main_service.domain.dto.GoodsCreateRequest;
import alien.marshmallow.main_service.domain.dto.GoodsDto;
import alien.marshmallow.main_service.domain.dto.GoodsUpdateRequest;
import alien.marshmallow.main_service.domain.entity.GoodsEntity;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GoodsMapper {

  GoodsDto toDto(GoodsEntity entity);

  List<GoodsDto> toDtoList(List<GoodsEntity> entities);

  @Mapping(target = "id", ignore = true)
  GoodsEntity toEntity(GoodsCreateRequest req);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void merge(@MappingTarget GoodsEntity entity, GoodsUpdateRequest req);

}
