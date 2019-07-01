package timecurvemanager.domain.balance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApprovedBalanceNotFoundException extends RuntimeException {

  private ApprovedBalanceNotFoundException(Long id, EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId) {
    super("Approved Balance not found for id: " + id + "dimension: " + dimension + ", timecurveId: "
        + timecurveId + ", itemType: " + itemType + ", itemId: " + itemId);
  }

  public static ApprovedBalanceNotFoundException approvedBalanceNotFound(Long id,
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId) {
    return new ApprovedBalanceNotFoundException(id, dimension, timecurveId, itemType, itemId);
  }

}
