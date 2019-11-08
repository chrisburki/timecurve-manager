package timecurvemanager.bookkeeping.application;

import static timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationAddException.objectTimecurveRelationAdd;
import static timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationNotFoundException.objectTimecurveRelationNotFound;
import static timecurvemanager.bookkeeping.domain.timecurve.TimecurveAddException.timecurveAddException;
import static timecurvemanager.bookkeeping.domain.timecurve.TimecurveNotFoundException.timecurveNotFound;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelation;
import timecurvemanager.bookkeeping.domain.objecttimecurve.ObjectTimecurveRelationRepository;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveRepository;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;

@Service
@Slf4j
public class TimecurveService {

  private final TimecurveRepository timecurveRepository;
  private final ObjectTimecurveRelationRepository relationRepository;
  private final TimecurveSpi timecurveSpi;

  public static final LocalDate minDate = LocalDate.of(1900, 01, 01);
  public static final LocalDate maxDate = LocalDate.of(4712, 12, 31);

  public TimecurveService(TimecurveRepository timecurveRepository,
      ObjectTimecurveRelationRepository relationRepository,
      TimecurveSpi timecurveSpi) {
    this.timecurveRepository = timecurveRepository;
    this.relationRepository = relationRepository;
    this.timecurveSpi = timecurveSpi;
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
    List<ObjectTimecurveRelation> relationList = relationRepository
        .findByObjectIdOrderByValidFromAsc(objectId);
    return relationList.stream().map(relation -> relation.getTimecurve())
        .collect(Collectors.toList());
  }

  /*
   * get Timecurve by Object & Date
   * *******************************
   * */
  public Timecurve getTimecurveByObjectIdAndDate(String objectId, LocalDate refDate) {
    ObjectTimecurveRelation relation = relationRepository
        .findByObjectIdAndRefDate(objectId, refDate)
        .orElseThrow(() -> objectTimecurveRelationNotFound(objectId, null, refDate));
    return relation.getTimecurve();
  }

  /*
   * get Object by Object & Date
   * *******************************
   * */
  public String getObjectByTimecuveIdAndDate(Long timecurveId, LocalDate refDate) {
    ObjectTimecurveRelation relation = relationRepository
        .findByTimecurveIdAndRefDate(timecurveId, refDate)
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
  private Timecurve saveTimecurve(Timecurve timecurve) {

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
  private Timecurve saveTimecurve(String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    Timecurve timecurve = new Timecurve(null, tenantId, name,
        clearingReference, needBalanceApproval);

    // save Timecurve
    return saveTimecurve(timecurve);
  }

  /*
   * Create Object Timecurve Relation & Timecurve passing in Timecurve returning Timecurve
   * *************************************************************************************
   * */
  private Timecurve createTimecurve(String objectId, Timecurve timecurve, LocalDate refDate) {

    List<ObjectTimecurveRelation> relations = relationRepository
        .findByObjectIdOrderByValidFromAsc(objectId);

    for (ObjectTimecurveRelation r : relations) {
      if (!refDate.isBefore(r.getValidFrom()) && !refDate.isAfter(r.getValidTo())) {
        return r.getTimecurve();
      }
    }

    ObjectTimecurveRelation relation = new ObjectTimecurveRelation(null, objectId,
        null, minDate, maxDate);
    timecurve.addTimecurveRelation(relation);
    try {
      timecurve = saveTimecurve(timecurve);
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
  public Long createTimecurve(String objectId, LocalDate refDate) {

    Timecurve timecurve = getObjectAndCreateTimecurve(objectId, refDate);

    return timecurve.getId();
  }

  /*
   * Find or Create Object Timecurve Relation & Timecurve passing in object and reference Date
   * *****************************************************************************************
   * */
  private Timecurve createTimecurve(String objectId, String tenantId,
      String clearingReference, Boolean needBalanceApproval, LocalDate refDate) {
    String name = "TIMECURVE: " + clearingReference;

    return createTimecurve(objectId, new Timecurve(null, tenantId, name, clearingReference,
        needBalanceApproval), refDate);
  }

  private Timecurve getObjectAndCreateTimecurve(String objectId, LocalDate refDate) {
    TimecurveObjectDetail timecurveObjectDetail = timecurveSpi.getObject(objectId);
    log.debug("Created Timecurve: " + timecurveObjectDetail.toString());
    return createTimecurve(objectId, timecurveObjectDetail.getTenantId(),
        timecurveObjectDetail.getClearingReference(), timecurveObjectDetail.getNeedBalanceApproval(), refDate);
  }

  public Timecurve addTimecurve(String objectId, LocalDate refDate) {
    Optional<Timecurve> timecurve = timecurveRepository.findByObjectIdAndRefDate(objectId, refDate);
    if (timecurve.isPresent()) {
      return timecurve.get();
    } else {
      return getObjectAndCreateTimecurve(objectId, refDate);
    }

//    return timecurveRepository.findByObjectRefDate(objectId, refDate)
//        .orElse(createTimecurve(objectId, refDate));

  }

}

