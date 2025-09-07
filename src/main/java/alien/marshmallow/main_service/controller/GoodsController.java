package alien.marshmallow.main_service.controller;

import alien.marshmallow.main_service.domain.dto.GoodsCreateRequest;
import alien.marshmallow.main_service.domain.dto.GoodsDto;
import alien.marshmallow.main_service.domain.dto.GoodsUpdateRequest;
import alien.marshmallow.main_service.service.GoodsService;
import alien.marshmallow.shared.annotations.SuccessResponse;
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
  public GoodsDto create(@RequestBody @Valid GoodsCreateRequest req) {
    return service.create(req);
  }

  @PutMapping("/{id}")
  public GoodsDto update(@PathVariable UUID id, @RequestBody @Valid GoodsUpdateRequest req) {
    return service.update(id, req);
  }

  @GetMapping("/{id}")
  public GoodsDto get(@PathVariable UUID id) {
    return service.findOne(id);
  }

  @GetMapping
  public List<GoodsDto> list() {
    return service.findAll();
  }

  @DeleteMapping("/{id}")
  @SuccessResponse(status = HttpStatus.NO_CONTENT, message = "Deleted successfully")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
