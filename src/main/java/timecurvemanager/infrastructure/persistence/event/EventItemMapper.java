package timecurvemanager.infrastructure.persistence.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectMapper;

@Component
public class EventItemMapper {

  private final EventMapper eventMapper;
  private final TimecurveObjectMapper timecurveMapper;

  public EventItemMapper(EventMapper eventMapper,
      TimecurveObjectMapper timecurveMapper) {
    this.eventMapper = eventMapper;
    this.timecurveMapper = timecurveMapper;
  }

  public EventItemEntity mapDomainToEntity(EventItem item) {

    return new EventItemEntity(eventMapper.mapDomainToEntity(item.getEvent()), item.getRowNr(),
        item.getTenantId(), item.getDimension(), timecurveMapper.mapDomainToEntity(item.getTimecurve()), item.getItemType(),
        item.getItemId(), item.getDate1(), item.getDate2(), item.getValue1(), item.getValue2(),
        item.getValue3(), item.getTover1(), item.getTover2(), item.getTover3());
  }

  public List<EventItemEntity> mapDomainToEntityList(List<EventItem> objectList) {
    return objectList.stream().map((eventItem) -> mapDomainToEntity(eventItem))
        .collect(Collectors.toList());
  }

  public EventItem mapEntityToDomain(EventItemEntity entity) {
    return new EventItem(entity.getId(), eventMapper.mapEntityToDomain(entity.getEventEntity()),
        entity.getRowNr(), entity.getTenantId(), entity.getDimension(), timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()),
        entity.getItemType(), entity.getItemId(), entity.getDate1(), entity.getDate2(),
        entity.getValue1(), entity.getValue2(), entity.getValue3(), entity.getTover1(),
        entity.getTover2(), entity.getTover3(), null);
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
          .of(new EventItem(entity.getId(), eventMapper.mapEntityToDomain(entity.getEventEntity()),
              entity.getRowNr(), entity.getTenantId(), entity.getDimension(),
              timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getItemType(), entity.getItemId(), entity.getDate1(),
              entity.getDate2(), entity.getValue1(), entity.getValue2(), entity.getValue3(),
              entity.getTover1(), entity.getTover2(), entity.getTover3(), null));
    } else {
      return Optional.empty();
    }
  }

}

