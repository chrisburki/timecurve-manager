package timecurvemanager.infrastructure.persistence.positiontimecurve;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.positiontimecurve.PositionTimecurveRelation;
import timecurvemanager.infrastructure.persistence.position.PositionEntityRepository;
import timecurvemanager.infrastructure.persistence.position.PositionMapper;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntityRepository;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectMapper;

@Component
public class PositionTimecurveRelationMapper {

  private final PositionMapper positionMapper;
  private final PositionEntityRepository positionEntityRepository;
  private final TimecurveObjectMapper timecurveMapper;
  private final TimecurveObjectEntityRepository timecurveObjectEntityRepository;

  public PositionTimecurveRelationMapper(
      PositionMapper positionMapper,
      PositionEntityRepository positionEntityRepository,
      TimecurveObjectMapper timecurveMapper,
      TimecurveObjectEntityRepository timecurveObjectEntityRepository) {
    this.positionMapper = positionMapper;
    this.positionEntityRepository = positionEntityRepository;
    this.timecurveMapper = timecurveMapper;
    this.timecurveObjectEntityRepository = timecurveObjectEntityRepository;
  }

  public PositionTimecurveRelationEntity mapDomainToEntity(PositionTimecurveRelation relation) {
    return new PositionTimecurveRelationEntity(
        positionEntityRepository.findById(relation.getPosition().getId()).get(),
        timecurveObjectEntityRepository.findById(relation.getTimecurve().getId()).get(),
        relation.getValidFrom(), relation.getValidTo());
  }

  public List<PositionTimecurveRelationEntity> mapDomainToEntityList(
      List<PositionTimecurveRelation> relationList) {
    return relationList.stream().map((relation) -> mapDomainToEntity(relation))
        .collect(Collectors.toList());
  }

  public PositionTimecurveRelation mapEntityToDomain(PositionTimecurveRelationEntity entity) {
    return new PositionTimecurveRelation(entity.getId(),
        positionMapper.mapEntityToDomain(entity.getPositionEntity()),
        timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getValidFrom(),
        entity.getValidTo());
  }

  public List<PositionTimecurveRelation> mapEntityToDomainList(
      List<PositionTimecurveRelationEntity> entityList) {
    return entityList.stream()
        .map((relationEntity) -> mapEntityToDomain(relationEntity))
        .collect(Collectors.toList());
  }

  public Optional<PositionTimecurveRelation> mapOptionalEntityToDomain(
      Optional<PositionTimecurveRelationEntity> positionEntity) {
    if (positionEntity.isPresent()) {
      PositionTimecurveRelationEntity entity = positionEntity.get();
      return Optional.of(new PositionTimecurveRelation(entity.getId(),
          positionMapper.mapEntityToDomain(entity.getPositionEntity()),
          timecurveMapper.mapEntityToDomain(entity.getTimecurveEntity()), entity.getValidFrom(),
          entity.getValidTo()));
    } else {
      return Optional.empty();
    }
  }

}
