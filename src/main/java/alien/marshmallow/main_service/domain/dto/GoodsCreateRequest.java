package alien.marshmallow.main_service.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GoodsCreateRequest {
  @NotBlank
  private String name;

  private String description;

  @NotNull
  @Digits(integer = 15, fraction = 4)
  @DecimalMin(value = "0.0000")
  private BigDecimal pricePerUnit;

  @NotNull
  @Min(0)
  private Long stockInUnits;
}
