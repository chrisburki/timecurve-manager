package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;

public class EventItemRepositoryImpl implements EventItemRepository {

  private final EventItemEntityRepository eventItemEntityRepository;
  private final EventItemMapper eventItemMapper;

  public EventItemRepositoryImpl(
      EventItemEntityRepository eventItemEntityRepository,
      EventItemMapper eventItemMapper) {
    this.eventItemEntityRepository = eventItemEntityRepository;
    this.eventItemMapper = eventItemMapper;
  }

  @Override
  public List<EventItem> findAll() {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository.findAll());
  }

  @Override
  public Optional<EventItem> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public List<EventItem> findByEvent(Event event) {
    return null;
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate) {
    return null;
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate) {
    return null;
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2) {
    return null;
  }

  @Override
  public EventItem save(EventItem eventItem) {
    return null;
  }
}
