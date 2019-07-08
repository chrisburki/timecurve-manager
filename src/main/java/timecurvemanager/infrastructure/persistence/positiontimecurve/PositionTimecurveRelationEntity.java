package timecurvemanager.infrastructure.persistence.positiontimecurve;

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
import org.springframework.stereotype.Component;
import timecurvemanager.infrastructure.persistence.position.PositionEntity;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntity;

@Entity
@Table(name = "position_timecurve", indexes = @Index(name = "idx_position_date",
    columnList = "position_id, valid_from, valid_to", unique = true))
@Getter
@NoArgsConstructor
@ToString
public class PositionTimecurveRelationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "position_id", referencedColumnName = "id", nullable = false)
  @NotNull
  private PositionEntity positionEntity;

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

  public PositionTimecurveRelationEntity(
      @NotNull PositionEntity positionEntity,
      @NotNull TimecurveObjectEntity timecurveEntity,
      @NotNull LocalDate validFrom, @NotNull LocalDate validTo) {
    this.positionEntity = positionEntity;
    this.timecurveEntity = timecurveEntity;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
