package timecurvemanager.domain.event;

import java.time.LocalDate;

import java.util.List;

public interface EventItemRepository {

  List<EventItem> findQueryByEventExtId(Long eventExtId);

  List<EventItem> findQueryEventItems(EventDimension dimension, Long objectId,
      EventItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase);
}