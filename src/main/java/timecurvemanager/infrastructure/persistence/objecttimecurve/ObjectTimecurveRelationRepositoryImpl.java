package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationRepository;

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
  public Optional<ObjectTimecurveRelation> findByObjectRefDate(Long objectId,
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
