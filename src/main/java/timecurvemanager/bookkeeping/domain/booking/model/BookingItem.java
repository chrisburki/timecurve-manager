package timecurvemanager.bookkeeping.domain.booking.model;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@todo: replace primary key id with booking, rowNr

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BookingItem {

  private Long id;

  private Integer rowNr;

  private Long timecurveId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  @Setter
  private BigDecimal value1;

  @Setter
  private BigDecimal value2;

  @Setter
  private BigDecimal value3;

  @NotNull
  @Setter
  private BigDecimal tover1;

  @Setter
  private BigDecimal tover2;

  @Setter
  private BigDecimal tover3;

  private Long gsn;

  public void setIdToNull() {
    this.id = null;
  }

  public BookingItem(Integer rowNr,
      Long timecurveId,
      @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
      @NotNull BigDecimal value1, BigDecimal value2,
      BigDecimal value3, @NotNull BigDecimal tover1, BigDecimal tover2, BigDecimal tover3,
      Long gsn) {
    this.rowNr = rowNr;
    this.timecurveId = timecurveId;
    this.itemType = itemType;
    this.itemId = itemId;
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.tover1 = tover1;
    this.tover2 = tover2;
    this.tover3 = tover3;
    this.gsn = gsn;
  }
}
