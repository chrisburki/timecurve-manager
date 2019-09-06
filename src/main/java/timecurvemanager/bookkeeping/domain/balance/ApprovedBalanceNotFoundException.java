package timecurvemanager.bookkeeping.domain.balance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApprovedBalanceNotFoundException extends RuntimeException {

  private ApprovedBalanceNotFoundException(Long id, BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId) {
    super("Approved Balance not found for id: " + id + "dimension: " + dimension + ", timecurveId: "
        + timecurveId + ", itemType: " + itemType + ", itemId: " + itemId);
  }

  public static ApprovedBalanceNotFoundException approvedBalanceNotFound(Long id,
      BookKeepingDimension dimension, Long timecurveId, BookKeepingItemType itemType, Long itemId) {
    return new ApprovedBalanceNotFoundException(id, dimension, timecurveId, itemType, itemId);
  }

}
