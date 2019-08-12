package timecurvemanager.infrastructure.persistence.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;

@Component
@Slf4j
public class EventMapper {

  private final EventItemMapper eventItemMapper;

  public EventMapper(
      EventItemMapper eventItemMapper) {
    this.eventItemMapper = eventItemMapper;
  }

  public EventEntity mapDomainToEntity(Event event) {
    EventEntity entity = new EventEntity(event.getEventExtId(), event.getSequenceNr(),
        event.getOrderId(), event.getTenantId(), event.getDimension(), event.getStatus(),
        event.getUseCase(), event.getDate1(), event.getDate2());
    event.getEventItems().forEach(item -> {
      entity.addEventItem(eventItemMapper.mapDomainToEntity(item));
    });
    return entity;
  }

  public List<EventEntity> mapDomainToEntityList(List<Event> eventList) {
    return eventList.stream().map((event) -> mapDomainToEntity(event))
        .collect(Collectors.toList());
  }

  public Event mapEntityToDomain(EventEntity entity) {
    Event event = new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
        entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
        entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn());
    entity.getEventItems().forEach(item -> {
      event.addEventItem2(eventItemMapper.mapEntityToDomain(item));
    });
    return event;
  }

  public Event mapEntityToDomain2(EventEntity entity) {
    return new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
        entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
        entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn());
  }

  public List<Event> mapEntityToDomainList(List<EventEntity> entityList) {
    return entityList.stream()
        .map((eventEntity) -> mapEntityToDomain2(eventEntity))
        .collect(Collectors.toList());
  }

  public Optional<Event> mapOptionalEntityToDomain(
      Optional<EventEntity> eventEntity) {
    if (eventEntity.isPresent()) {
      EventEntity entity = eventEntity.get();
      return Optional.of(new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
          entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
          entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn()));
    } else {
      return Optional.empty();
    }
  }

}
