package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntityRepository;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectMapper;

@Component
public class ObjectTimecurveRelationMapper {


  private final TimecurveObjectMapper timecurveMapper;
  private final TimecurveObjectEntityRepository timecurveObjectEntityRepository;

  public ObjectTimecurveRelationMapper(
      TimecurveObjectMapper timecurveMapper,
      TimecurveObjectEntityRepository timecurveObjectEntityRepository) {
    this.timecurveMapper = timecurveMapper;
    this.timecurveObjectEntityRepository = timecurveObjectEntityRepository;
  }

  public ObjectTimecurveRelationEntity mapDomainToEntity(ObjectTimecurveRelation relation) {
    return new ObjectTimecurveRelationEntity(
        relation.getObjectId(),
        timecurveObjectEntityRepository.findById(relation.getTimecurve().getId()).get(),
        relation.getValidFrom(), relation.getValidTo());
  }

  public List<ObjectTimecurveRelationEntity> mapDomainToEntityList(
      List<ObjectTimecurveRelation> relationList) {
    return relationList.stream().map((relation) -> mapDomainToEntity(relation))
        .collect(Collectors.toList());
  }

  public ObjectTimecurveRelation mapEntityToDomain(ObjectTimecurveRelationEntity entity) {
    return new ObjectTimecurveRelation(entity.getId(),
        entity.getObjectId(),
        timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getValidFrom(),
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
          timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getValidFrom(),
          entity.getValidTo()));
    } else {
      return Optional.empty();
    }
  }

}
