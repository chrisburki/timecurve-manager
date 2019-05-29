package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;

@Component
public class EventItemRepositoryImpl implements EventItemRepository {

  private final EventItemEntityRepository eventItemEntityRepository;
  private final EventItemMapper eventItemMapper;
  private final EventMapper eventMapper;

  public EventItemRepositoryImpl(
      EventItemEntityRepository eventItemEntityRepository,
      EventItemMapper eventItemMapper,
      EventMapper eventMapper) {
    this.eventItemEntityRepository = eventItemEntityRepository;
    this.eventItemMapper = eventItemMapper;
    this.eventMapper = eventMapper;
  }

  @Override
  public List<EventItem> findAll() {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository.findAll());
  }

  @Override
  public Optional<EventItem> findById(Long id) {
    return eventItemMapper
        .mapOptionalEntityToDomain(eventItemEntityRepository.findById(id));
  }

  @Override
  public List<EventItem> findByEvent(Event event) {
    return eventItemMapper
        .mapEntityToDomainList(eventItemEntityRepository.findByEventEntity(eventMapper.mapDomainToEntity(event)));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1Between(dimension, timecurveId, itemType, itemId, fromDate, toDate));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate2Between(dimension, timecurveId, itemType, itemId, fromDate, toDate));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(dimension, timecurveId, itemType, itemId, fromDate1, toDate1, fromDate2, toDate2));
  }

  @Override
  public EventItem save(EventItem eventItem) {
    return eventItemMapper.mapEntityToDomain(eventItemEntityRepository
        .save(eventItemMapper.mapDomainToEntity(eventItem)));
  }
}
