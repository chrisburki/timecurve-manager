package timecurvemanager.domain.event.view;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;

@Getter
@AllArgsConstructor
@ToString
public class EventTurnover {

  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private Long timecurveId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  @Setter
  private BigDecimal toverValue1;

  @Setter
  private BigDecimal toverValue2;

  @Setter
  private BigDecimal toverValue3;

  @Setter
  private Long maxGsn;
}
