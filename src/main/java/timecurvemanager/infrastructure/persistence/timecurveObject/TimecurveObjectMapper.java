package timecurvemanager.infrastructure.persistence.timecurveObject;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.timecurveObject.TimecurveObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TimecurveObjectMapper {

  public TimecurveObjectEntity mapDomainToEntity(TimecurveObject object) {
    return new TimecurveObjectEntity(object.getTenantId(), object.getTag(), object.getName(),
        object.getValueType(), object.getValueTag(), object.getClearingReference(), object.getNeedBalanceApproval());
  }

  public List<TimecurveObjectEntity> mapDomainToEntityList(List<TimecurveObject> objectList) {
    return objectList.stream().map((timecurve) -> mapDomainToEntity(timecurve))
        .collect(Collectors.toList());
  }

  public TimecurveObject mapEntityToDomain(TimecurveObjectEntity entity) {
    return new TimecurveObject(entity.getId(), entity.getTenantId(), entity.getTag(),
        entity.getName(), entity.getValueType(), entity.getValueTag(),
        entity.getClearingReference(), entity.getNeedBalanceApproval());
  }

  public List<TimecurveObject> mapEntityToDomainList(List<TimecurveObjectEntity> entityList) {
    return entityList.stream()
        .map((timecurveObjectEntity) -> mapEntityToDomain(timecurveObjectEntity))
        .collect(Collectors.toList());
  }

  public Optional<TimecurveObject> mapOptionalEntityToDomain(
      Optional<TimecurveObjectEntity> timecurveEntity) {
    if (timecurveEntity.isPresent()) {
      TimecurveObjectEntity entity = timecurveEntity.get();
      return Optional.of(new TimecurveObject(entity.getId(), entity.getTenantId(), entity.getTag(),
          entity.getName(), entity.getValueType(), entity.getValueTag(),
          entity.getClearingReference(), entity.getNeedBalanceApproval()));
    } else {
      return Optional.empty();
    }
  }
}
