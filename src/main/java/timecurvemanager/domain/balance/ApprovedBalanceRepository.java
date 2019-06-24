package timecurvemanager.domain.balance;

import java.util.Optional;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

public interface ApprovedBalanceRepository {

  Optional<ApprovedBalance> findById(Long id);

  Optional<ApprovedBalance> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      EventDimension dimension,
      Long timecurveId, EventItemType itemType, Long itemId);

  ApprovedBalance save(ApprovedBalance balance);
}
