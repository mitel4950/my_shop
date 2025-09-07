package alien.marshmallow.main_service.service;

import alien.marshmallow.main_service.domain.dto.GoodsCreateRequest;
import alien.marshmallow.main_service.domain.dto.GoodsDto;
import alien.marshmallow.main_service.domain.dto.GoodsUpdateRequest;
import alien.marshmallow.main_service.domain.entity.GoodsEntity;
import alien.marshmallow.main_service.mapper.GoodsMapper;
import alien.marshmallow.main_service.repository.GoodsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsService {

  private final GoodsRepository repository;
  private final GoodsMapper mapper;

  @Transactional
  public GoodsDto create(GoodsCreateRequest req) {
    GoodsEntity entity = mapper.toEntity(req);
    GoodsEntity saved = repository.save(entity);
    return mapper.toDto(saved);
  }

  @Transactional
  public GoodsDto update(UUID id, GoodsUpdateRequest req) {
    GoodsEntity existing = repository.getRequired(id);
    mapper.merge(existing, req);
    GoodsEntity saved = repository.save(existing);
    return mapper.toDto(saved);
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(repository.getRequired(id));
  }

  @Transactional(readOnly = true)
  public GoodsDto findOne(UUID id) {
    GoodsEntity exist = repository.getRequired(id);
    return mapper.toDto(exist);
  }

  @Transactional(readOnly = true)
  public List<GoodsDto> findAll() {
    return mapper.toDtoList(repository.findAll());
  }
}
