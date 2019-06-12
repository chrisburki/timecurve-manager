package timecurvemanager.domain.balance;

import java.math.BigDecimal;

import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

public class ApprovedBalance {

  private Long id;

  private EventDimension dimension;

  private Long timecurveId;

  private EventItemType itemType;

  private Long itemId;

  private BigDecimal value1;

  public ApprovedBalance(Long id, EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId, BigDecimal value1) {
    this.id = id;
    this.dimension = dimension;
    this.timecurveId = timecurveId;
    this.itemType = itemType;
    this.itemId = itemId;
    this.value1 = value1;
  }

  public Long getId() {
    return id;
  }

  public EventDimension getDimension() {
    return dimension;
  }

  public Long getTimecurveId() {
    return timecurveId;
  }

  public EventItemType getItemType() {
    return itemType;
  }

  public Long getItemId() {
    return itemId;
  }

  public BigDecimal getValue1() {
    return value1;
  }

  @Override
  public String toString() {
    return "ApprovedBalance{" +
        "id=" + id +
        ", dimension=" + dimension +
        ", timecurveId=" + timecurveId +
        ", itemType=" + itemType +
        ", itemId=" + itemId +
        ", value1=" + value1 +
        '}';
  }
}
