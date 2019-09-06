package timecurvemanager.bookkeeping.infrastructure.persistence.timecurve;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.bookkeeping.infrastructure.persistence.objecttimecurve.ObjectTimecurveRelationEntity;

@Entity
@Table(name = "timecurve_object")
@Getter
@NoArgsConstructor
@ToString
public class TimecurveEntity {

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

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "timecurveEntity", orphanRemoval = true)
  private List<ObjectTimecurveRelationEntity> timecurveRelations = new ArrayList<>();

  public TimecurveEntity(String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    this.tenantId = tenantId;
    this.name = name;
    this.clearingReference = clearingReference;
    this.needBalanceApproval = needBalanceApproval;
  }

  public void addTimecurveRelation(ObjectTimecurveRelationEntity relationEntity) {
    this.timecurveRelations.add(relationEntity);
    relationEntity.setTimecurveEntity(this);
  }

}

