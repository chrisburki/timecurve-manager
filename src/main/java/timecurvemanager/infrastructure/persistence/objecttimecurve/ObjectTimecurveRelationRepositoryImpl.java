package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationRepository;
import timecurvemanager.domain.position.Position;

@Component
public class ObjectTimecurveRelationRepositoryImpl implements
    ObjectTimecurveRelationRepository {

  private final ObjectTimecurveRelationEntityRepository relationEntityRepository;
  private final ObjectTimecurveRelationMapper relationMapper;

  public ObjectTimecurveRelationRepositoryImpl(
      ObjectTimecurveRelationEntityRepository relationEntityRepository,
      ObjectTimecurveRelationMapper relationMapper) {
    this.relationEntityRepository = relationEntityRepository;
    this.relationMapper = relationMapper;
  }

  @Override
  public List<ObjectTimecurveRelation> findByObjectId(String objectId) {
    return relationMapper.mapEntityToDomainList(relationEntityRepository.findByObjectId(objectId));
  }

  @Override
  public Optional<ObjectTimecurveRelation> findByObjectRefDate(String objectId,
      LocalDate refDate) {
    return relationMapper
        .mapOptionalEntityToDomain(
            relationEntityRepository.findByObjectRefDate(objectId, refDate));
  }

  @Override
  public ObjectTimecurveRelation save(ObjectTimecurveRelation objectTimecurveRelation) {
    return relationMapper.mapEntityToDomain(
        relationEntityRepository.save(relationMapper.mapDomainToEntity(objectTimecurveRelation)));
  }

  @Override
  public void delete(ObjectTimecurveRelation relation) {
    relationEntityRepository
        .delete(relationMapper.mapDomainToEntity(relation));
  }
}
