package timecurvemanager.bookkeeping.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationRepository;

@Component
@Slf4j
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
  public Optional<ObjectTimecurveRelation> findByTimecurveAndRefDate(Long timecurveId,
      LocalDate refDate) {
    return relationMapper.mapOptionalEntityToDomain(
        relationEntityRepository.findByTimecurveAndRefDate(timecurveId, refDate));
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
