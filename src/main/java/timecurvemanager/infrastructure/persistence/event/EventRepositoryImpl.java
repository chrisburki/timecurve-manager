package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventRepository;

public class EventRepositoryImpl implements EventRepository {

  private final EventEntityRepository eventEntityRepository;
  private final EventMapper eventMapper;

  public EventRepositoryImpl(EventEntityRepository eventEntityRepository,
      EventMapper eventMapper) {
    this.eventEntityRepository = eventEntityRepository;
    this.eventMapper = eventMapper;
  }

  @Override
  public List<Event> findAll() {
    return eventMapper.mapEntityToDomainList(eventEntityRepository.findAll());
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
  public List<Event> findByDimensionAndDate1BetweenAndUseCase(EventDimension dimension,
      LocalDate fromDate, LocalDate toDate, String useCase) {
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findByDimensionAndDate1BetweenAndUseCase(dimension, fromDate, toDate, useCase));
  }

  @Override
  public List<Event> findByDimensionAndDate2BetweenAndUseCase(EventDimension dimension,
      LocalDate fromDate, LocalDate toDate, String useCase) {
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findByDimensionAndDate2BetweenAndUseCase(dimension, fromDate, toDate, useCase));
  }

  @Override
  public List<Event> findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(
      EventDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase) {
    return eventMapper.mapEntityToDomainList(eventEntityRepository
        .findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(dimension, fromDate1, toDate1,
            fromDate2, toDate2, useCase));
  }

  @Override
  public Event save(Event event) {
    return eventMapper.mapEntityToDomain(eventEntityRepository
        .save(eventMapper.mapDomainToEntity(event)));
  }
}
