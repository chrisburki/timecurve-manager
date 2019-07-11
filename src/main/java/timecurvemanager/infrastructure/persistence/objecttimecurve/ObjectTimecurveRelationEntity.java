package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntity;

@Entity
@Table(name = "object_timecurve_relation", indexes = @Index(name = "idx_object_date",
    columnList = "object_id, valid_from, valid_to", unique = true))
@Getter
@NoArgsConstructor
@ToString
public class ObjectTimecurveRelationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "object_id")
  private Long objectId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timecurve_id", referencedColumnName = "id", nullable = false)
  @NotNull
  private TimecurveObjectEntity timecurveEntity;

  @NotNull
  @Column(name = "valid_from")
  LocalDate validFrom;

  @NotNull
  @Column(name = "valid_to")
  LocalDate validTo;

  public ObjectTimecurveRelationEntity(
      Long objectId,
      @NotNull TimecurveObjectEntity timecurveEntity,
      @NotNull LocalDate validFrom, @NotNull LocalDate validTo) {
    this.objectId = objectId;
    this.timecurveEntity = timecurveEntity;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
