package timecurvemanager.application;

import static timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationAddException.objectTimecurveRelationAdd;
import static timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationNotFoundException.objectTimecurveRelationNotFound;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelationRepository;
import timecurvemanager.domain.timecurve.Timecurve;

@Service
@Slf4j
public class ObjectTimecurveRelationService {

  private final ObjectTimecurveRelationRepository relationRepository;
  private final TimecurveService timecurveService;

  private final LocalDate maxDate = LocalDate.of(4712, 12, 31);

  public ObjectTimecurveRelationService(
      ObjectTimecurveRelationRepository relationRepository,
      TimecurveService timecurveService) {
    this.relationRepository = relationRepository;
    this.timecurveService = timecurveService;
  }

  /*
   * Helper - nvl
   */
  private <T> T nvl(T arg0, T arg1) {
    return (arg0 == null) ? arg1 : arg0;
  }


  /*
   * get Timecurves by Object
   * ************************
   * */
  public Collection<Timecurve> listTimecuves(String objectId) {
    List<ObjectTimecurveRelation> relationList = relationRepository.findByObjectId(objectId);
    return relationList.stream().map(relation -> relation.getTimecurve())
        .collect(Collectors.toList());
  }

  /*
   * get Timecurve by Object & Date
   * *******************************
   * */
  public Timecurve getTimecurveByObjectAndDateId(String objectId, LocalDate refDate) {
    ObjectTimecurveRelation relation = relationRepository.findByObjectRefDate(objectId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(objectId, refDate));
    return relation.getTimecurve();
  }

  /*
   * Create Object Timecurve Relation & Timecurve
   * ********************************************
   * create a ObjectTimecurveRelation & Timecurve and return the timecurve
   * */
  public Timecurve createTimecurve(String objectId, Timecurve timecurve, LocalDate refDate) {

    ObjectTimecurveRelation relation = new ObjectTimecurveRelation(null, objectId,
        null, nvl(refDate,LocalDate.now()), maxDate);
    timecurve.addTimecurveRelation(relation);
    timecurve = timecurveService.addTimecurve(timecurve);
//    try {
//      relationRepository.save(relation);
//    } catch (DataAccessException ex) {
//      throw objectTimecurveRelationAdd(objectId, relation.getTimecurve().getId(),
//          refDate);
//    }
    return timecurve;
  }


  // internal: get Object Timecurve Relation
  public ObjectTimecurveRelation getByObjectAndRefDate(String objectId, LocalDate refDate) {
    return relationRepository.findByObjectRefDate(objectId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(objectId, refDate));
  }


  // internal: only for manual corrections
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


  // internal: only for manual corrections
  @Transactional
  public void delete(ObjectTimecurveRelation relation) {
    try {
      relationRepository.delete(relation);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationNotFound(relation.getObjectId(), relation.getValidFrom());
    }
  }
}
