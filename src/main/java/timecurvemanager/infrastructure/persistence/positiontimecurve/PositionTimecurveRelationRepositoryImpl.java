package timecurvemanager.infrastructure.persistence.positiontimecurve;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.positiontimecurve.PositionTimecurveRelation;
import timecurvemanager.domain.positiontimecurve.PositionTimecurveRelationRepository;

@Component
public class PositionTimecurveRelationRepositoryImpl implements
    PositionTimecurveRelationRepository {

  private final PositionTimecurveRelationEntityRepository relationEntityRepository;
  private final PositionTimecurveRelationMapper relationMapper;

  public PositionTimecurveRelationRepositoryImpl(
      PositionTimecurveRelationEntityRepository relationEntityRepository,
      PositionTimecurveRelationMapper relationMapper) {
    this.relationEntityRepository = relationEntityRepository;
    this.relationMapper = relationMapper;
  }

  @Override
  public Optional<PositionTimecurveRelation> findByPositionRefDate(Long positionId,
      LocalDate refDate) {
    return relationMapper
        .mapOptionalEntityToDomain(relationEntityRepository.findByPositionRefDate(positionId, refDate));
  }

  @Override
  public PositionTimecurveRelation save(PositionTimecurveRelation positionTimecurveRelation) {
    return relationMapper.mapEntityToDomain(relationEntityRepository.save(relationMapper.mapDomainToEntity(positionTimecurveRelation)));
  }

  @Override
  public void delete(PositionTimecurveRelation relation) {
    relationEntityRepository
        .delete(relationMapper.mapDomainToEntity(relation));
  }
}
