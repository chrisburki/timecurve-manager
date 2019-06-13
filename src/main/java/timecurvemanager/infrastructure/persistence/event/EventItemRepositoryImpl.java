package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectMapper;

@Component
public class EventItemRepositoryImpl implements EventItemRepository {

  private final EventItemEntityRepository eventItemEntityRepository;
  private final EventItemMapper eventItemMapper;
  private final EventMapper eventMapper;
  private final TimecurveObjectMapper timecurveMapper;

  public EventItemRepositoryImpl(
      EventItemEntityRepository eventItemEntityRepository,
      EventItemMapper eventItemMapper,
      EventMapper eventMapper,
      TimecurveObjectMapper timecurveMapper) {
    this.eventItemEntityRepository = eventItemEntityRepository;
    this.eventItemMapper = eventItemMapper;
    this.eventMapper = eventMapper;
    this.timecurveMapper = timecurveMapper;
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
        .mapEntityToDomainList(
            eventItemEntityRepository.findByEventEntity(eventMapper.mapDomainToEntity(event)));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType,
      Example<Long> itemId,
      LocalDate fromDate, LocalDate toDate) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate1Between(
            dimension, timecurveMapper.mapDomainToEntity(timecurve), itemType, itemId, fromDate,
            toDate));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType,
      Example<Long> itemId,
      LocalDate fromDate, LocalDate toDate) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate2Between(
            dimension, timecurveMapper.mapDomainToEntity(timecurve), itemType, itemId, fromDate,
            toDate));
  }

  @Override
  public List<EventItem> findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType,
      Example<Long> itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
            dimension, timecurveMapper.mapDomainToEntity(timecurve), itemType, itemId, fromDate1,
            toDate1, fromDate2, toDate2));
  }

}
