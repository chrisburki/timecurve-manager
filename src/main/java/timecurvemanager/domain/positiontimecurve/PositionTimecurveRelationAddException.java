package timecurvemanager.domain.positiontimecurve;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class PositionTimecurveRelationAddException extends RuntimeException {

  private PositionTimecurveRelationAddException(Long positionId, Long timecurveId,
      LocalDate refDate) {
    super("Position Timecurve Realtion not found for PositionId: " + positionId
        + " and reference date: " + refDate);
  }

  public static PositionTimecurveRelationAddException positionTimecurveRelationAdd(
      Long positionId, Long timecurveId, LocalDate refDate) {
    return new PositionTimecurveRelationAddException(positionId, timecurveId, refDate);
  }
}
