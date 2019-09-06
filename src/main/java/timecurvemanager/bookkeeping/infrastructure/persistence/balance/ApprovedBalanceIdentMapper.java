package timecurvemanager.bookkeeping.infrastructure.persistence.balance;

import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Component
public class ApprovedBalanceIdentMapper {

  public ApprovedBalanceEntityIdent mapDomainToEntity(BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId) {
    return new ApprovedBalanceEntityIdent(dimension, timecurveId, itemType, itemId);
  }

}
