package timecurvemanager.domain.balance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApprovedBalanceNotFoundException extends RuntimeException {

  private ApprovedBalanceNotFoundException(EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId) {
    super("Approved Balance not found for dimension: " + dimension + ", timecurveId: " + timecurveId
        + ", itemType: " + itemType + ", itemId: " + itemId);
  }

  public static ApprovedBalanceNotFoundException approvedBalanceNotFound(EventDimension dimension,
      Long timecurveId,
      EventItemType itemType, Long itemId) {
    return new ApprovedBalanceNotFoundException(dimension, timecurveId, itemType, itemId);
  }

}
