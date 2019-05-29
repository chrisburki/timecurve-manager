package timecurvemanager.infrastructure.persistence.balance;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Component
public class ApprovedBalanceIdentMapper {

  public ApprovedBalanceEntityIdent mapDomainToEntity(EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId) {
    return new ApprovedBalanceEntityIdent(dimension, timecurveId, itemType, itemId);
  }

}
