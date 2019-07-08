package timecurvemanager.domain.positiontimecurve;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

@Getter
@AllArgsConstructor
@ToString
public class PositionTimecurveRelation {

  private Long id;

  private Position position;

  private TimecurveObject timecurve;

  LocalDate validFrom;

  LocalDate validTo;

}
