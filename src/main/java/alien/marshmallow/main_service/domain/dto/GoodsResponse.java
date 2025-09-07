package alien.marshmallow.main_service.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class GoodsResponse {
  private UUID id;
  private String name;
  private String description;
  private BigDecimal pricePerUnit;
  private Long stockInUnits;
}
