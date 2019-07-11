package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

@Getter
@AllArgsConstructor
@ToString
public class ObjectTimecurveRelation {

  private Long id;

  private Long objectId;

  private TimecurveObject timecurve;

  LocalDate validFrom;

  LocalDate validTo;

}
