package timecurvemanager.bookkeeping.domain.balance;

import java.util.Optional;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

public interface ApprovedBalanceRepository {

  Optional<ApprovedBalance> findById(Long id);

  Optional<ApprovedBalance> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId);

  ApprovedBalance save(ApprovedBalance balance);
}
