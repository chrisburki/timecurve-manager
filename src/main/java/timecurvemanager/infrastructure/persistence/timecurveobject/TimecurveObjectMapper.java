package timecurvemanager.infrastructure.persistence.timecurveobject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

@Component
public class TimecurveObjectMapper {

  public TimecurveObjectEntity mapDomainToEntity(TimecurveObject object) {
    return new TimecurveObjectEntity(object.getTenantId(), object.getName(),
        object.getClearingReference(), object.getNeedBalanceApproval());
  }

  public List<TimecurveObjectEntity> mapDomainToEntityList(List<TimecurveObject> objectList) {
    return objectList.stream().map((timecurve) -> mapDomainToEntity(timecurve))
        .collect(Collectors.toList());
  }

  public TimecurveObject mapEntityToDomain(TimecurveObjectEntity entity) {
    return new TimecurveObject(entity.getId(), entity.getTenantId(),
        entity.getName(), entity.getClearingReference(), entity.getNeedBalanceApproval());
  }

  public List<TimecurveObject> mapEntityToDomainList(List<TimecurveObjectEntity> entityList) {
    return entityList.stream()
        .map((objectEntity) -> mapEntityToDomain(objectEntity))
        .collect(Collectors.toList());
  }

  public Optional<TimecurveObject> mapOptionalEntityToDomain(
      Optional<TimecurveObjectEntity> objectEntity) {
    if (objectEntity.isPresent()) {
      TimecurveObjectEntity entity = objectEntity.get();
      return Optional.of(new TimecurveObject(entity.getId(), entity.getTenantId(), entity.getName(),
          entity.getClearingReference(), entity.getNeedBalanceApproval()));
    } else {
      return Optional.empty();
    }
  }
}
