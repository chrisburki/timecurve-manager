package timecurvemanager.bookkeeping.application;

import static timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationAddException.objectTimecurveRelationAdd;
import static timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationNotFoundException.objectTimecurveRelationNotFound;
import static timecurvemanager.bookkeeping.domain.timecurve.TimecurveAddException.timecurveAddException;
import static timecurvemanager.bookkeeping.domain.timecurve.TimecurveNotFoundException.timecurveNotFound;

import java.time.LocalDate;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationRepository;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveRepository;

@Service
@Slf4j
public class TimecurveService {

  private final TimecurveRepository timecurveRepository;
  private final ObjectTimecurveRelationRepository relationRepository;

  private final LocalDate maxDate = LocalDate.of(4712, 12, 31);

  public TimecurveService(TimecurveRepository timecurveRepository,
      ObjectTimecurveRelationRepository relationRepository) {
    this.timecurveRepository = timecurveRepository;
    this.relationRepository = relationRepository;
  }

  //
  // helper
  //
  private <T> T nvl(T arg0, T arg1) {
    return (arg0 == null) ? arg1 : arg0;
  }


  //
  // query
  //

  /*
   * list Timecuve Objects
   * *********************
   * */
  public Collection<Timecurve> listObjects() {
    return timecurveRepository.findAll();
  }

  /*
   * get Timecurve Object
   * ********************
   * */
  public Timecurve getById(Long id) {
    return timecurveRepository.findById(id)
        .orElseThrow(() -> timecurveNotFound(id.toString()));

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
  public Timecurve getTimecurveByObjectIdAndDate(String objectId, LocalDate refDate) {
    ObjectTimecurveRelation relation = relationRepository.findByObjectRefDate(objectId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(objectId, null, refDate));
    return relation.getTimecurve();
  }

  /*
   * get Object by Object & Date
   * *******************************
   * */
  public String getObjectByTimecuveIdAndDate(Long timecurveId, LocalDate refDate) {
    ObjectTimecurveRelation relation = relationRepository
        .findByTimecurveAndRefDate(timecurveId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(null, timecurveId, refDate));
    return relation.getObjectId();
  }


  //
  // Post
  //

  /*
   * add Timecurve for an object
   * ***************************
   * */
  @Transactional
  public Timecurve addTimecurve(Timecurve timecurve) {

    // save Timecurve
    try {
      return timecurveRepository.save(timecurve);
    } catch (DataAccessException ex) {
      throw timecurveAddException(timecurve.getId());
    }
  }

  /*
   * add Timecurve for relevant attributes
   * *************************************
   * */
  public Timecurve addTimecurve(String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    Timecurve timecurve = new Timecurve(null, tenantId, name,
        clearingReference, needBalanceApproval);

    // save Timecurve
    return addTimecurve(timecurve);
  }

  /*
   * Create Object Timecurve Relation & Timecurve passing in Timecurve returning Timecurve
   * *************************************************************************************
   * */
  public Timecurve createTimecurve(String objectId, Timecurve timecurve, LocalDate refDate) {
    ObjectTimecurveRelation relation = new ObjectTimecurveRelation(null, objectId,
        null, nvl(refDate, LocalDate.now()), maxDate);
    timecurve.addTimecurveRelation(relation);
    try {
      timecurve = addTimecurve(timecurve);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationAdd(objectId, relation.getTimecurve().getId(),
          refDate);
    }
    return timecurve;
  }

  /*
   * Create Object Timecurve Relation & Timecurve passing in relevant attributes returning Timecurve
   * ***********************************************************************************************
   * */
  public Timecurve createTimecurve(String objectId, String tenantId, String name,
      String clearingReference, Boolean needBalanceApproval, LocalDate refDate) {
    Timecurve timecurve = new Timecurve(null, tenantId, name, clearingReference,
        needBalanceApproval);
    ObjectTimecurveRelation relation = new ObjectTimecurveRelation(null, objectId,
        null, nvl(refDate, LocalDate.now()), maxDate);
    timecurve.addTimecurveRelation(relation);
    try {
      timecurve = addTimecurve(timecurve);
    } catch (DataAccessException ex) {
      throw objectTimecurveRelationAdd(objectId, relation.getTimecurve().getId(),
          refDate);
    }
    return timecurve;
  }

  /*
   * Create Object Timecurve Relation & Timecurve passing in object
   * **************************************************************
   * */
  public Long createTimecurve(String objectId, LocalDate refDate) {
    //@todo: implement using spi
    // 1. get object details
    // 2. get create timecurve object
    // 3. create relation & add to timecurve object
    // 4. add timecurve
    return null;
  }

}

