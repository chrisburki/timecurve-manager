package timecurvemanager.infrastructure.persistence.balance;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "approved_balance",
    indexes = {
        @Index(name = "approved_balance_idx",
            columnList = "dimension, timecurve_id, item_type, item_id", unique = true)
    })
@Getter
@NoArgsConstructor
@ToString
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
  @NotNull
  private BigDecimal value1;

  public ApprovedBalanceEntity(Long id,
      ApprovedBalanceEntityIdent approvedBalanceEntityIdent,
      BigDecimal value1) {
    this.id = id;
    this.approvedBalanceEntityIdent = approvedBalanceEntityIdent;
    this.value1 = value1;
  }

}
