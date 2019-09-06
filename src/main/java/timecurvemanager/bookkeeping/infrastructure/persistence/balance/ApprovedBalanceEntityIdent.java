package timecurvemanager.bookkeeping.infrastructure.persistence.balance;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
public class ApprovedBalanceEntityIdent implements Serializable {

  @NotNull
  private BookKeepingDimension dimension;

  @Column(name = "timecurve_id")
  @NotNull
  private Long timecurveId;

  @Column(name = "item_type")
  @NotNull
  private BookKeepingItemType itemType;

  @Column(name = "item_id")
  private Long itemId;

  public ApprovedBalanceEntityIdent(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId) {
    this.dimension = dimension;
    this.timecurveId = timecurveId;
    this.itemType = itemType;
    this.itemId = itemId;
  }

}
