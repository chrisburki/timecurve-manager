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
  public List<ObjectTimecurveRelation> findByObjectIdOrderByValidFromAsc(String objectId) {
    return relationMapper.mapEntityToDomainList(relationEntityRepository.findByObjectIdOrderByValidFromAsc(objectId));
  }

  @Override
  public Optional<ObjectTimecurveRelation> findByTimecurveIdAndRefDate(Long timecurveId,
      LocalDate refDate) {
    return relationMapper.mapOptionalEntityToDomain(
        relationEntityRepository.findByTimecurveIdAndRefDate(timecurveId, refDate));
  }

  @Override
  public Optional<ObjectTimecurveRelation> findByObjectIdAndRefDate(String objectId,
      LocalDate refDate) {
    return relationMapper
        .mapOptionalEntityToDomain(
            relationEntityRepository.findByObjectIdAndRefDate(objectId, refDate));
  }

  @Override
  public void delete(ObjectTimecurveRelation relation) {
    relationEntityRepository
        .delete(relationMapper.mapDomainToEntity(relation));
  }
}
