package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.timecurve.Timecurve;

@Getter
@AllArgsConstructor
@ToString
public class ObjectTimecurveRelation {

  private Long id;

  private String objectId;

  @Setter
  private Timecurve timecurve;

  LocalDate validFrom;

  LocalDate validTo;

}
