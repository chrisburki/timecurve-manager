package timecurvemanager.infrastructure.persistence.balance;

import java.math.BigDecimal;
import javax.persistence.*;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;


@Entity
@Table(name = "approved_balance"
    , indexes = {
    @Index(name = "approved_balance_idx", columnList = "dimension, timecurve_id, item_type, item_id", unique = true)
})
public class ApprovedBalanceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private EventDimension dimension;

  @Column(name = "timecurve_id")
  private Long timecurveId;

  @Column(name = "item_type")
  private EventItemType itemType;

  @Column(name = "item_id")
  private Long itemId;

  private BigDecimal value1;

  public ApprovedBalanceEntity(EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId, BigDecimal value1) {
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
    return "ApprovedBalanceEntity{" +
        "id=" + id +
        ", dimension=" + dimension +
        ", timecurveId=" + timecurveId +
        ", itemType=" + itemType +
        ", itemId=" + itemId +
        ", value1=" + value1 +
        '}';
  }
}
