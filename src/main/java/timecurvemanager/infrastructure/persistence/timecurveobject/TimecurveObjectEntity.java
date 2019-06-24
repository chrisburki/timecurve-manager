package timecurvemanager.infrastructure.persistence.timecurveobject;

import javax.persistence.Column;
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
import timecurvemanager.domain.timecurveobject.TimecurveObjectValueType;

@Entity
@Table(name = "timecurve_object", indexes = @Index(name = "idx_timecurve_label",
    columnList = "tag", unique = true))
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
  private String tag;

  @NotNull
  private String name;

  @Column(name = "value_type")
  @NotNull
  private TimecurveObjectValueType valueType;

  @Column(name = "value_tag")
  @NotNull
  private String valueTag;

  @Column(name = "clearing_reference")
  private String clearingReference;

  @Column(name = "need_balance_approval")
  @NotNull
  private Boolean needBalanceApproval;

  public TimecurveObjectEntity(String tenantId, String tag, String name,
      TimecurveObjectValueType valueType, String valueTag, String clearingReference,
      Boolean needBalanceApproval) {
    this.tenantId = tenantId;
    this.tag = tag;
    this.name = name;
    this.valueType = valueType;
    this.valueTag = valueTag;
    this.clearingReference = clearingReference;
    this.needBalanceApproval = needBalanceApproval;
  }

}

