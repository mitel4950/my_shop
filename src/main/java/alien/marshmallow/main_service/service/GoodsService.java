package alien.marshmallow.main_service.service;

import alien.marshmallow.main_service.domain.dto.GoodsCreateRequest;
import alien.marshmallow.main_service.domain.dto.GoodsResponse;
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
  public GoodsResponse create(GoodsCreateRequest req) {
    GoodsEntity entity = mapper.fromCreate(req);
    GoodsEntity saved = repository.save(entity);
    return mapper.toResponse(saved);
  }

  @Transactional
  public GoodsResponse update(UUID id, GoodsUpdateRequest req) {
    GoodsEntity entity = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Goods not found: " + id));

    mapper.merge(entity, req);

    GoodsEntity saved = repository.save(entity);
    return mapper.toResponse(saved);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Goods not found: " + id);
    }
    repository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public GoodsResponse findOne(UUID id) {
    return repository.findById(id)
        .map(mapper::toResponse)
        .orElseThrow(() -> new IllegalArgumentException("Goods not found: " + id));
  }

  @Transactional(readOnly = true)
  public List<GoodsResponse> findAll() {
    return mapper.toResponseList(repository.findAll());
  }
}
