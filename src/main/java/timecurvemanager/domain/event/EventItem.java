package timecurvemanager.domain.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.timecurve.Timecurve;

//@todo: replace primary key id with event, rowNr

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventItem {

  private Long id;

  @Setter
  private Event event;

  private Integer rowNr;

  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private Timecurve timecurve;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  private LocalDate date1;

  @NotNull
  private LocalDate date2;

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

  private Long approvedBalanceId;

  public void setIdToNull() {
    this.id = null;
  }

  public EventItem(Event event, Integer rowNr, @NotNull String tenantId,
      @NotNull BookKeepingDimension dimension, @NotNull Timecurve timecurve,
      @NotNull BookKeepingItemType itemType, @NotNull Long itemId, @NotNull LocalDate date1,
      @NotNull LocalDate date2, @NotNull BigDecimal value1, BigDecimal value2,
      BigDecimal value3, @NotNull BigDecimal tover1, BigDecimal tover2, BigDecimal tover3,
      Long gsn, Long approvedBalanceId) {
    this.event = event;
    this.rowNr = rowNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.timecurve = timecurve;
    this.itemType = itemType;
    this.itemId = itemId;
    this.date1 = date1;
    this.date2 = date2;
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.tover1 = tover1;
    this.tover2 = tover2;
    this.tover3 = tover3;
    this.gsn = gsn;
    this.approvedBalanceId = approvedBalanceId;
  }
}
