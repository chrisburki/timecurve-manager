package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventRepository;

@Component
@Slf4j
public class EventRepositoryImpl implements EventRepository {

  private final EventEntityRepository eventEntityRepository;
  private final EventMapper eventMapper;
  private final EventExtIdSequenceRepository eventExtIdSequenceRepository;

  public EventRepositoryImpl(EventEntityRepository eventEntityRepository,
      EventMapper eventMapper,
      EventExtIdSequenceRepository eventExtIdSequenceRepository) {
    this.eventEntityRepository = eventEntityRepository;
    this.eventMapper = eventMapper;
    this.eventExtIdSequenceRepository = eventExtIdSequenceRepository;
  }

  @Override
  public Optional<Event> findById(Long id) {
    return eventMapper
        .mapOptionalEntityToDomain(eventEntityRepository.findById(id));
  }

  @Override
  public Optional<Event> findByEventExtId(Long eventExtId) {
    return eventMapper
        .mapOptionalEntityToDomain(eventEntityRepository.findByEventExtId(eventExtId));
  }

  @Override
  public Optional<Event> findEventItemByEventExtId(Long eventExtId) {
    return eventMapper.mapOptionalEntityToDomain(eventEntityRepository.findEventItemByEventExtId(eventExtId));
  }

  @Override
  public List<Event> findQueryEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    log.info(
        "fromDate1: " + fromDate1 + "toDate: " + toDate1 + "fromDate2: " + fromDate2 + "toDate2: "
            + toDate2);
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findQueryEvents(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase));
  }

  @Override
  public List<Event> findEventItemsQueryEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findEventItemsQueryEvents(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase));
  }

  @Override
  public Event save(Event event) {
    return eventMapper.mapEntityToDomain(eventEntityRepository
        .save(eventMapper.mapDomainToEntity(event)));
  }

  @Override
  public Long getNextEventExtId() {
    EventExtIdSequence extIdSequence = new EventExtIdSequence();
    return eventExtIdSequenceRepository.save(extIdSequence).getId();
  }
}
