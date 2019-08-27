package timecurvemanager.infrastructure.persistence.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.model.Event;
import timecurvemanager.domain.event.model.EventItem;

@Component
@Slf4j
public class EventItemMapper {

  public EventItemEntity mapDomainToEntity(EventItem item) {
    return new EventItemEntity(item.getRowNr(),
        item.getTenantId(), item.getDimension(),
        item.getTimecurveId(),
        item.getItemType(),
        item.getItemId(), item.getDate1(), item.getDate2(), item.getValue1(), item.getValue2(),
        item.getValue3(), item.getTover1(), item.getTover2(), item.getTover3());
  }

  public List<EventItemEntity> mapDomainToEntityList(List<EventItem> itemList) {
    return itemList.stream().map(eventItem -> mapDomainToEntity(eventItem))
        .collect(Collectors.toList());
  }

  /* return EventItems given Event*/
  public EventItem mapEntityAndEventToDomain(EventItemEntity entity, Event event) {
    return new EventItem(entity.getId(), event,
        entity.getRowNr(), entity.getTenantId(), entity.getDimension(), entity.getTimecurveId(),
        entity.getItemType(), entity.getItemId(), entity.getDate1(), entity.getDate2(),
        entity.getValue1(), entity.getValue2(), entity.getValue3(), entity.getTover1(),
        entity.getTover2(), entity.getTover3(), entity.getGsn());
  }


  private Event mapEventEntityToDomain(EventEntity entity) {
    return new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
        entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
        entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn());
  }

  public EventItem mapEntityToDomain(EventItemEntity entity) {
    return new EventItem(entity.getId(), mapEventEntityToDomain(entity.getEventEntity()),
        entity.getRowNr(), entity.getTenantId(), entity.getDimension(), entity.getTimecurveId(),
        entity.getItemType(), entity.getItemId(), entity.getDate1(), entity.getDate2(),
        entity.getValue1(), entity.getValue2(), entity.getValue3(), entity.getTover1(),
        entity.getTover2(), entity.getTover3(), entity.getGsn());
  }

  public List<EventItem> mapEntityToDomainList(List<EventItemEntity> entityList) {
    return entityList.stream()
        .map((eventItemEntity) -> mapEntityToDomain(eventItemEntity))
        .collect(Collectors.toList());
  }

  public Optional<EventItem> mapOptionalEntityToDomain(
      Optional<EventItemEntity> eventItemEntity) {
    if (eventItemEntity.isPresent()) {
      EventItemEntity entity = eventItemEntity.get();
      return Optional
          .of(new EventItem(entity.getId(), mapEventEntityToDomain(entity.getEventEntity()),
              entity.getRowNr(), entity.getTenantId(), entity.getDimension(),
              entity.getTimecurveId(), entity.getItemType(), entity.getItemId(), entity.getDate1(),
              entity.getDate2(), entity.getValue1(), entity.getValue2(), entity.getValue3(),
              entity.getTover1(), entity.getTover2(), entity.getTover3(), entity.getGsn()));
    } else {
      return Optional.empty();
    }
  }

}

