package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.infrastructure.persistence.timecurve.TimecurveEntityRepository;
import timecurvemanager.infrastructure.persistence.timecurve.TimecurveMapper;

@Component
@Slf4j
public class ObjectTimecurveRelationMapper {


  public ObjectTimecurveRelationEntity mapDomainToEntity(ObjectTimecurveRelation relation) {
    return new ObjectTimecurveRelationEntity(relation.getObjectId(), relation.getValidFrom(),
        relation.getValidTo());
  }

  public List<ObjectTimecurveRelationEntity> mapDomainToEntityList(
      List<ObjectTimecurveRelation> relationList) {
    return relationList.stream().map((relation) -> mapDomainToEntity(relation))
        .collect(Collectors.toList());
  }

  public ObjectTimecurveRelation mapEntityToDomain(ObjectTimecurveRelationEntity entity) {
    return new ObjectTimecurveRelation(entity.getId(),
        entity.getObjectId(),
        null, entity.getValidFrom(),
        entity.getValidTo());
  }

  public List<ObjectTimecurveRelation> mapEntityToDomainList(
      List<ObjectTimecurveRelationEntity> entityList) {
    return entityList.stream()
        .map((relationEntity) -> mapEntityToDomain(relationEntity))
        .collect(Collectors.toList());
  }

  public Optional<ObjectTimecurveRelation> mapOptionalEntityToDomain(
      Optional<ObjectTimecurveRelationEntity> relationEntity) {
    if (relationEntity.isPresent()) {
      ObjectTimecurveRelationEntity entity = relationEntity.get();
      return Optional.of(new ObjectTimecurveRelation(entity.getId(),
          entity.getObjectId(),
          null, entity.getValidFrom(),
          entity.getValidTo()));
    } else {
      return Optional.empty();
    }
  }

}
