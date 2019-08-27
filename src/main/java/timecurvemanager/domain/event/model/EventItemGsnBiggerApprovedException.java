package timecurvemanager.domain.event.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class EventItemGsnBiggerApprovedException extends RuntimeException {

  private EventItemGsnBiggerApprovedException(Long lastGsn, Long approvedGsn) {
    super("Last Gsn: " + lastGsn + " is bigger then apprved Gsn: " + approvedGsn);
  }

  public static EventItemGsnBiggerApprovedException EventItemGsnBiggerApproved(Long lastGsn,
      Long approvedGsn) {
    return new EventItemGsnBiggerApprovedException(lastGsn, approvedGsn);
  }
}
