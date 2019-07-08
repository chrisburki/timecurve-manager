package timecurvemanager.application;

import static timecurvemanager.domain.positiontimecurve.PositionTimecurveRelationAddException.positionTimecurveRelationAdd;
import static timecurvemanager.domain.positiontimecurve.PositionTimecurveRelationNotFoundException.positionTimecurveRelationNotFound;

import java.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionValueType;
import timecurvemanager.domain.positiontimecurve.PositionTimecurveRelation;
import timecurvemanager.domain.positiontimecurve.PositionTimecurveRelationRepository;

@Service
public class PositionTimecurveRelationService {

  private final PositionTimecurveRelationRepository relationRepository;
  private final TimecurveObjectService timecurveObjectService;

  public PositionTimecurveRelationService(
      PositionTimecurveRelationRepository relationRepository,
      TimecurveObjectService timecurveObjectService) {
    this.relationRepository = relationRepository;
    this.timecurveObjectService = timecurveObjectService;
  }

  /*
   * get Position Timecurve Relation
   * *******************************
   * */
  public PositionTimecurveRelation getByPositionAndRefDate(Long positionId, LocalDate refDate) {
    return relationRepository.findByPositionRefDate(positionId, refDate)
        .orElseThrow(() -> positionTimecurveRelationNotFound(positionId, refDate));
  }

  /*
   * add Position Timecurve Relation
   * *******************************
   * search for an existing based on the position and reference date. if not find create a PositionTimecurveRelation and return it
   * (only used by Position Service)
   * */
  private String getClearingReference(Position position) {
    if (position.getValueType() == PositionValueType.CURRENCY) {
      return position.getValueTag();
    } else {
      return null;
    }
  }

  private Boolean getNeedBalanceApproval(Position position) {
    return position.getValueType() == PositionValueType.CURRENCY;
  }

  private PositionTimecurveRelation createPositionTimecurveRelation(Position position,
      LocalDate refDate) {
    PositionTimecurveRelation relation = new PositionTimecurveRelation(null, position,
        timecurveObjectService.addTimecurve(position.getTenantId(),
            position.getTenantId() + ":" + position.getValueType() + ":" + position.getValueTag(),
            getClearingReference(position), getNeedBalanceApproval(position)),
        refDate, LocalDate.MIN);
    try {
      return relationRepository.save(relation);
    } catch (DataAccessException ex) {
      throw positionTimecurveRelationAdd(position.getId(), relation.getTimecurve().getId(),
          refDate);
    }
  }

  public PositionTimecurveRelation addPositionTimecurveRelation(Position position,
      LocalDate refDate) {

    return relationRepository
        .findByPositionRefDate(position.getId(), refDate)
        .orElse(createPositionTimecurveRelation(position, refDate));

  }

  // only for manual corrections
  @Transactional
  public PositionTimecurveRelation addPositionTimecurveRelation(
      PositionTimecurveRelation relation) {
    try {
      return relationRepository.save(relation);
    } catch (DataAccessException ex) {
      throw positionTimecurveRelationAdd(relation.getPosition().getId(),
          relation.getTimecurve().getId(), relation.getValidFrom());
    }

  }


  // only for manual corrections
  @Transactional
  public void delete(PositionTimecurveRelation relation) {
    try {
      relationRepository.delete(relation);
    } catch (DataAccessException ex) {
      throw positionTimecurveRelationNotFound(relation.getPosition().getId(),
          relation.getValidFrom());
    }
  }
}
