package timecurvemanager.domain.balance;

import java.util.Optional;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;

public interface ApprovedBalanceRepository {

  Optional<ApprovedBalance> findById(Long id);

  Optional<ApprovedBalance> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId);

  ApprovedBalance save(ApprovedBalance balance);
}
