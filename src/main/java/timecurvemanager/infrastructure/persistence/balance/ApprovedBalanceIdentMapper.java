package timecurvemanager.infrastructure.persistence.balance;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;

@Component
public class ApprovedBalanceIdentMapper {

  public ApprovedBalanceEntityIdent mapDomainToEntity(BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId) {
    return new ApprovedBalanceEntityIdent(dimension, timecurveId, itemType, itemId);
  }

}
