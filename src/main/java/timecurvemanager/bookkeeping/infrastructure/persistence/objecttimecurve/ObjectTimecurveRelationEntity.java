package timecurvemanager.bookkeeping.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
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
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.bookkeeping.infrastructure.persistence.timecurve.TimecurveEntity;

//@todo:check indexes
@Entity
@Table(name = "object_timecurve_relation",
    indexes = {
        @Index(name = "idx_object_date", columnList = "object_id, valid_from, valid_to", unique = true),
        @Index(name = "idx_timecurve_date", columnList = "timecurve_id, valid_from, valid_to", unique = true)
    }
)
@Getter
@NoArgsConstructor
@ToString
public class ObjectTimecurveRelationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "object_id")
  private String objectId;

  @ManyToOne
  @JoinColumn(name = "timecurve_id", referencedColumnName = "id", nullable = false)
  @Setter
  @NotNull
  private TimecurveEntity timecurveEntity;

  @NotNull
  @Column(name = "valid_from")
  LocalDate validFrom;

  @NotNull
  @Column(name = "valid_to")
  LocalDate validTo;

  public ObjectTimecurveRelationEntity(
      @NotNull String objectId,
      @NotNull LocalDate validFrom, @NotNull LocalDate validTo) {
    this.objectId = objectId;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
