package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.infrastructure.persistence.timecurve.TimecurveMapper;

@Component
public class EventItemRepositoryImpl implements EventItemRepository {

  private final EventItemEntityRepository eventItemEntityRepository;
  private final EventItemMapper eventItemMapper;
  private final TimecurveMapper timecurveMapper;

  public EventItemRepositoryImpl(
      EventItemEntityRepository eventItemEntityRepository,
      EventItemMapper eventItemMapper,
      TimecurveMapper timecurveMapper) {
    this.eventItemEntityRepository = eventItemEntityRepository;
    this.eventItemMapper = eventItemMapper;
    this.timecurveMapper = timecurveMapper;
  }

  @Override
  public List<EventItem> findQueryByEventExtId(Long eventExtId) {
    return eventItemMapper
        .mapEntityToDomainList(eventItemEntityRepository.findQueryByEventExtId(eventExtId));
  }

  @Override
  public List<EventItem> findQueryEventItems(EventDimension dimension, Long objectId,
      EventItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findQueryEventItems(dimension, objectId, itemType, itemId, fromDate1, toDate1, fromDate2,
            toDate2, useCase));
  }

}
