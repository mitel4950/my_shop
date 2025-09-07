package alien.marshmallow.main_service.controller;

import alien.marshmallow.main_service.domain.dto.GoodsCreateRequest;
import alien.marshmallow.main_service.domain.dto.GoodsResponse;
import alien.marshmallow.main_service.domain.dto.GoodsUpdateRequest;
import alien.marshmallow.main_service.service.GoodsService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/goods", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GoodsController {

  private final GoodsService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public GoodsResponse create(@RequestBody @Valid GoodsCreateRequest req) {
    return service.create(req);
  }

  @PutMapping("/{id}")
  public GoodsResponse update(@PathVariable UUID id, @RequestBody @Valid GoodsUpdateRequest req) {
    return service.update(id, req);
  }

  @GetMapping("/{id}")
  public GoodsResponse get(@PathVariable UUID id) {
    return service.findOne(id);
  }

  @GetMapping
  public List<GoodsResponse> list() {
    return service.findAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
