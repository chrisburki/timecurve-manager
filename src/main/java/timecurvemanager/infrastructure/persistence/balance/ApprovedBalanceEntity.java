package timecurvemanager.infrastructure.persistence.balance;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "approved_balance",
    indexes = {
        @Index(name = "approved_balance_idx", columnList = "dimension, timecurve_id, item_type, item_id", unique = true)
    })
public class ApprovedBalanceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  //  @EmbeddedId
  private ApprovedBalanceEntityIdent approvedBalanceEntityIdent;

  /*
  private EventDimension dimension;

  @Column(name = "timecurve_id")
  private Long timecurveId;

  @Column(name = "item_type")
  private EventItemType itemType;

  @Column(name = "item_id")
  private Long itemId;
*/
  private BigDecimal value1;

  public ApprovedBalanceEntity(
      ApprovedBalanceEntityIdent approvedBalanceEntityIdent,
      BigDecimal value1) {
    this.approvedBalanceEntityIdent = approvedBalanceEntityIdent;
    this.value1 = value1;
  }

  public Long getId() {
    return id;
  }

  public ApprovedBalanceEntityIdent getApprovedBalanceEntityIdent() {
    return approvedBalanceEntityIdent;
  }

  public BigDecimal getValue1() {
    return value1;
  }


  @Override
  public String toString() {
    return "ApprovedBalanceEntity{" +
        "id=" + id +
        ", approvedBalanceEntityIdent=" + approvedBalanceEntityIdent +
        ", value1=" + value1 +
        '}';
  }
}
