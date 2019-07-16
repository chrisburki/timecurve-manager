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
  public Optional<Event> findLastByEventExtId(Long eventExtId) {
    return eventMapper
        .mapOptionalEntityToDomain(eventEntityRepository.findLastByEventExtId(eventExtId));
  }

  @Override
  public List<Event> findQueryEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findByFilterCriteria(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase));
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
