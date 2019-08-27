package timecurvemanager.infrastructure.persistence.timecurve;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.timecurve.Timecurve;
import timecurvemanager.infrastructure.persistence.objecttimecurve.ObjectTimecurveRelationMapper;

@Component
@Slf4j
public class TimecurveMapper {

  private final ObjectTimecurveRelationMapper relationMapper;

  public TimecurveMapper(
      ObjectTimecurveRelationMapper relationMapper) {
    this.relationMapper = relationMapper;
  }

  public TimecurveEntity mapDomainToEntity(Timecurve timecurve) {
    TimecurveEntity entity = new TimecurveEntity(timecurve.getTenantId(), timecurve.getName(),
        timecurve.getClearingReference(), timecurve.getNeedBalanceApproval());
    timecurve.getTimecurveRelations().forEach(relation -> {
      entity.addTimecurveRelation(relationMapper.mapDomainToEntity(relation));
    });
    return entity;
  }

  public List<TimecurveEntity> mapDomainToEntityList(List<Timecurve> objectList) {
    return objectList.stream().map((timecurve) -> mapDomainToEntity(timecurve))
        .collect(Collectors.toList());
  }

  public Timecurve mapEntityToDomain(TimecurveEntity entity) {
    Timecurve timecurve = new Timecurve(entity.getId(), entity.getTenantId(),
        entity.getName(), entity.getClearingReference(), entity.getNeedBalanceApproval());
    entity.getTimecurveRelations().forEach(relation -> {
      timecurve.addTimecurveRelation(relationMapper.mapEntityToDomain(relation));
    });
    return timecurve;
  }

  public Timecurve mapEntityToDomain2(TimecurveEntity entity) {
    return new Timecurve(entity.getId(), entity.getTenantId(),
        entity.getName(), entity.getClearingReference(), entity.getNeedBalanceApproval());
  }

  public List<Timecurve> mapEntityToDomainList(List<TimecurveEntity> entityList) {
    return entityList.stream()
        .map((objectEntity) -> mapEntityToDomain2(objectEntity))
        .collect(Collectors.toList());
  }

  public Optional<Timecurve> mapOptionalEntityToDomain(
      Optional<TimecurveEntity> objectEntity) {
    if (objectEntity.isPresent()) {
      TimecurveEntity entity = objectEntity.get();
      return Optional.of(new Timecurve(entity.getId(), entity.getTenantId(), entity.getName(),
          entity.getClearingReference(), entity.getNeedBalanceApproval()));
    } else {
      return Optional.empty();
    }
  }
}
