package timecurvemanager.bookkeeping.domain.booking.api;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingCommandItem implements Serializable {

  @NotNull
  private Integer rowNr;

  @NotNull
  private String objectId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;
}
