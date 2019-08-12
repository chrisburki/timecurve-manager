package timecurvemanager.domain.event;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface EventItemRepository {

  List<EventItem> findQueryByEventExtId(Long eventExtId);

  List<EventItem> findQueryEventItems(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase);

  List<EventItem> findQueryByTimecurveIdAndGsnBetween(Long timecurveId,
      BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId, LocalDate maxDate1,
      LocalDate maxDate2, Long fromGsn, Long toGsn);

  Optional<EventItem> findFirstByTimecurveEntityIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(
      Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType
  );

  Long findQueryLastGsnByTimecurve(Long timecurveId, BookKeepingDimension dimension,
      BookKeepingItemType itemType, Long itemId);
}