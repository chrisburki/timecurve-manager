package timecurvemanager.infrastructure.persistence.balance;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
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

}
