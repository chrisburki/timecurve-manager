package timecurvemanager.infrastructure.persistence.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntityRepository;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectMapper;

@Component
public class EventItemMapper {

  private final TimecurveObjectMapper timecurveMapper;
  private final TimecurveObjectEntityRepository timecurveObjectEntityRepository;

  public EventItemMapper(TimecurveObjectMapper timecurveMapper,
      TimecurveObjectEntityRepository timecurveObjectEntityRepository) {
    this.timecurveMapper = timecurveMapper;
    this.timecurveObjectEntityRepository = timecurveObjectEntityRepository;
  }

  public EventItemEntity mapDomainToEntity(EventItem item) {

    return new EventItemEntity(item.getRowNr(),
        item.getTenantId(), item.getDimension(),
        timecurveObjectEntityRepository.findById(item.getTimecurve().getId()).get(),
        item.getItemType(),
        item.getItemId(), item.getDate1(), item.getDate2(), item.getValue1(), item.getValue2(),
        item.getValue3(), item.getTover1(), item.getTover2(), item.getTover3());
  }

  public List<EventItemEntity> mapDomainToEntityList(List<EventItem> objectList) {
    return objectList.stream().map((eventItem) -> mapDomainToEntity(eventItem))
        .collect(Collectors.toList());
  }

  private Event mapEventEntityToDomain(EventEntity entity) {
    return new Event(entity.getId(), entity.getEventExtId(), entity.getSequenceNr(),
        entity.getTenantId(), entity.getDimension(), entity.getStatus(), entity.getUseCase(),
        entity.getDate1(), entity.getDate2());
  }

  public EventItem mapEntityToDomain(EventItemEntity entity) {
    return new EventItem(entity.getId(), mapEventEntityToDomain(entity.getEventEntity()),
        entity.getRowNr(), entity.getTenantId(), entity.getDimension(),
        timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()),
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
          .of(new EventItem(entity.getId(), mapEventEntityToDomain(entity.getEventEntity()),
              entity.getRowNr(), entity.getTenantId(), entity.getDimension(),
              timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getItemType(),
              entity.getItemId(), entity.getDate1(),
              entity.getDate2(), entity.getValue1(), entity.getValue2(), entity.getValue3(),
              entity.getTover1(), entity.getTover2(), entity.getTover3(), null));
    } else {
      return Optional.empty();
    }
  }

}

