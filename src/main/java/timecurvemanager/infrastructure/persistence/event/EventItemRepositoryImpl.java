package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;
import timecurvemanager.domain.event.model.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.infrastructure.persistence.timecurve.TimecurveMapper;

@Component
@Slf4j
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
  public List<EventItem> findQueryEventItems(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    return eventItemMapper.mapEntityToDomainList(eventItemEntityRepository
        .findQueryEventItems(dimension, timecurveId, itemType, itemId, fromDate1, toDate1,
            fromDate2, toDate2, useCase));
  }

  @Override
  public List<EventItem> findQueryByTimecurveIdAndGsnBetween(Long timecurveId,
      BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId, LocalDate maxDate1,
      LocalDate maxDate2, Long fromGsn, Long toGsn) {
    return eventItemMapper.mapEntityToDomainList(
        (eventItemEntityRepository
            .findByTimecurveEntityAndGsnBetween(timecurveId, dimension, itemType, itemId, maxDate1,
                maxDate2, fromGsn, toGsn)));
  }

  @Override
  public Optional<EventItem> findFirstByTimecurveIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(
      Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType) {
    return eventItemMapper.mapOptionalEntityToDomain(eventItemEntityRepository
        .findFirstByTimecurveIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(
            timecurveId, dimension, itemType));
  }

  @Override
  public Long findQueryLastGsnByTimecurve(Long timecurveId,
      BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId) {
    return eventItemEntityRepository
        .findQueryLastGsnByTimecurve(timecurveId, dimension, itemType, itemId);
  }

}
