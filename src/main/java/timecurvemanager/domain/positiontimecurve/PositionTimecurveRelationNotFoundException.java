package timecurvemanager.domain.positiontimecurve;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PositionTimecurveRelationNotFoundException extends RuntimeException {

  private PositionTimecurveRelationNotFoundException(Long positionId, LocalDate refDate) {
    super("Position Timecurve Realtion not found for PositionId: " + positionId
        + " and reference date: " + refDate);
  }

  public static PositionTimecurveRelationNotFoundException positionTimecurveRelationNotFound(
      Long positionId, LocalDate refDate) {
    return new PositionTimecurveRelationNotFoundException(positionId, refDate);
  }

}
