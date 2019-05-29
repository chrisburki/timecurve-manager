package timecurvemanager.infrastructure.persistence.balance;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Embeddable
public class ApprovedBalanceEntityIdent implements Serializable {

  private EventDimension dimension;

  @Column(name = "timecurve_id")
  private Long timecurveId;

  @Column(name = "item_type")
  private EventItemType itemType;

  @Column(name = "item_id")
  private Long itemId;

  public ApprovedBalanceEntityIdent(EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId) {
    this.dimension = dimension;
    this.timecurveId = timecurveId;
    this.itemType = itemType;
    this.itemId = itemId;
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

  @Override
  public String toString() {
    return "ApprovedBalanceEntityIdent{" +
        "dimension=" + dimension +
        ", timecurveId=" + timecurveId +
        ", itemType=" + itemType +
        ", itemId=" + itemId +
        '}';
  }
}
