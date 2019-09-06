package timecurvemanager.bookkeeping.domain.booking.api;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Getter
@AllArgsConstructor
@ToString
public class BookingQueryTurnover {

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
