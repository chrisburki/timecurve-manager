package timecurvemanager.infrastructure.persistence.timecurveobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import timecurvemanager.domain.timecurveobject.TimecurveObjectValueType;

@Entity
@Table(name = "timecurve_object", indexes = @Index(name = "idx_timecurve_label", columnList = "tag", unique = true))
public class TimecurveObjectEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "tenant_id")
  private String tenantId;

  private String tag;

  private String name;

  @Column(name = "value_type")
  private TimecurveObjectValueType valueType;

  @Column(name = "value_tag")
  private String valueTag;

  @Column(name = "clearing_reference")
  private String clearingReference;

  @Column(name = "need_balance_approval")
  private Boolean needBalanceApproval;

  protected TimecurveObjectEntity() {
  }

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

  public Long getId() {
    return id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTag() {
    return tag;
  }

  public String getName() {
    return name;
  }

  public TimecurveObjectValueType getValueType() {
    return valueType;
  }

  public String getValueTag() {
    return valueTag;
  }

  public String getClearingReference() {
    return clearingReference;
  }

  public Boolean getNeedBalanceApproval() {
    return needBalanceApproval;
  }

  @Override
  public String toString() {
    return "TimecurveObjectEntity{" +
        "id=" + id +
        ", tenantId='" + tenantId + '\'' +
        ", tag='" + tag + '\'' +
        ", name='" + name + '\'' +
        ", valueType=" + valueType +
        ", valueTag='" + valueTag + '\'' +
        ", clearingReference='" + clearingReference + '\'' +
        ", needBalanceApproval=" + needBalanceApproval +
        '}';
  }
}

