package timecurvemanager.application;

import static timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationAddException.objectTimecurveRelationAdd;
import static timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationNotFoundException.objectTimecurveRelationNotFound;

import java.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationRepository;

@Service
public class ObjectTimecurveRelationService {

  private final ObjectTimecurveRelationRepository relationRepository;
  private final TimecurveObjectService timecurveObjectService;

  private final LocalDate maxDate = LocalDate.of(4712,12,31);

  public ObjectTimecurveRelationService(
      ObjectTimecurveRelationRepository relationRepository,
      TimecurveObjectService timecurveObjectService) {
    this.relationRepository = relationRepository;
    this.timecurveObjectService = timecurveObjectService;
  }

  /*
   * get Object Timecurve Relation
   * *******************************
   * */
  public ObjectTimecurveRelation getByObjectAndRefDate(Long objectId, LocalDate refDate) {
    return relationRepository.findByObjectRefDate(objectId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(objectId, refDate));
  }

  /*
   * add Object Timecurve Relation
   * *******************************
   * search for an existing based on the object and reference date. if not find
   * create a ObjectTimecurveRelation and return it
   * (only used by Object Services like Position Service)
   * */
  private ObjectTimecurveRelation createObjectTimecurveRelation(Long objectId,
      LocalDate refDate, String tenantId, String clearingReference, Boolean needBalanceApproval,
      String nameExtension) {
    ObjectTimecurveRelation relation = new ObjectTimecurveRelation(null, objectId,
        timecurveObjectService.addTimecurve(tenantId,
            tenantId + ":" + clearingReference + ":" + needBalanceApproval.toString() + ":"
                + nameExtension, clearingReference, needBalanceApproval
            ),
        refDate, maxDate);
    try {
      return relationRepository.save(relation);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationAdd(objectId, relation.getTimecurve().getId(),
          refDate);
    }
  }

  public ObjectTimecurveRelation addObjectTimecurveRelation(Long objectId, LocalDate refDate,
      String tenantId, String clearingReference, Boolean needBalanceApproval,
      String nameExtension) {

    return relationRepository
        .findByObjectRefDate(objectId, refDate)
        .orElse(createObjectTimecurveRelation(objectId, refDate, tenantId, clearingReference,
            needBalanceApproval, nameExtension));

  }

  // only for manual corrections
  @Transactional
  public ObjectTimecurveRelation addObjectTimecurveRelation(
      ObjectTimecurveRelation relation) {
    try {
      return relationRepository.save(relation);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationAdd(relation.getObjectId(),
          relation.getTimecurve().getId(), relation.getValidFrom());
    }

  }


  // only for manual corrections
  @Transactional
  public void delete(ObjectTimecurveRelation relation) {
    try {
      relationRepository.delete(relation);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationNotFound(relation.getObjectId(), relation.getValidFrom());
    }
  }
}
