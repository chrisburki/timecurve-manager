package timecurvemanager.infrastructure.persistence.timecurveobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "timecurve_object")
@Getter
@NoArgsConstructor
@ToString
public class TimecurveObjectEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "tenant_id")
  @NotNull
  private String tenantId;

  @NotNull
  private String name;

  @Column(name = "clearing_reference")
  private String clearingReference;

  @Column(name = "need_balance_approval")
  @NotNull
  private Boolean needBalanceApproval;

  public TimecurveObjectEntity(String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    this.tenantId = tenantId;
    this.name = name;
    this.clearingReference = clearingReference;
    this.needBalanceApproval = needBalanceApproval;
  }

}

