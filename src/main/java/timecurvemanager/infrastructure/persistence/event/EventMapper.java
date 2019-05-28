package timecurvemanager.infrastructure.persistence.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;

@Component
public class EventMapper {

  public EventEntity mapDomainToEntity(Event event) {
    return new EventEntity(event.getEventExtId(), event.getSequenceNr(), event.getTenantId(),
        event.getDimension(), event.getStatus(), event.getUseCase(), event.getDate1(),
        event.getDate2());
  }

  public List<EventEntity> mapDomainToEntityList(List<Event> objectList) {
    return objectList.stream().map((event) -> mapDomainToEntity(event))
        .collect(Collectors.toList());
  }

  public Event mapEntityToDomain(EventEntity entity) {
    return new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
        entity.getTenantId(), entity.getDimension(), entity.getStatus(), entity.getUseCase(),
        entity.getDate1(), entity.getDate2(), null);
  }

  public List<Event> mapEntityToDomainList(List<EventEntity> entityList) {
    return entityList.stream()
        .map((eventEntity) -> mapEntityToDomain(eventEntity))
        .collect(Collectors.toList());
  }

  public Optional<Event> mapOptionalEntityToDomain(
      Optional<EventEntity> eventEntity) {
    if (eventEntity.isPresent()) {
      EventEntity entity = eventEntity.get();
      return Optional.of(new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
          entity.getTenantId(), entity.getDimension(), entity.getStatus(), entity.getUseCase(),
          entity.getDate1(), entity.getDate2(), null));
    } else {
      return Optional.empty();
    }
  }

}
