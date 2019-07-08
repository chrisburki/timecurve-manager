package timecurvemanager.application;

import static timecurvemanager.domain.timecurveobject.TimecurveObjectAddException.timecurveObjectAddException;
import static timecurvemanager.domain.timecurveobject.TimecurveObjectNotFoundException.timecurveObjectNotFound;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.domain.timecurveobject.TimecurveObjectRepository;

@Service
public class TimecurveObjectService {

  private final TimecurveObjectRepository timecurveObjectRepository;

  public TimecurveObjectService(TimecurveObjectRepository timecurveObjectRepository) {
    this.timecurveObjectRepository = timecurveObjectRepository;
  }

  /*
   * list Timecuve Objects
   * *********************
   * */
  public Collection<TimecurveObject> listObjects() {
    return timecurveObjectRepository.findAll();
  }

  /*
   * get Timecurve Object
   * ********************
   * */
  public TimecurveObject getById(Long id) {
    return timecurveObjectRepository.findById(id)
        .orElseThrow(() -> timecurveObjectNotFound(id.toString()));

  }

  /*
   * add TimecurveObject
   * *************
   * search for an existing based on the label. if not find create a new object and return it
   * (only used by Position Timecurve Relation Service resp. by Position Service)
   * */
  public TimecurveObject addTimecurve(String tenantId, String name, String clearingReference, Boolean needBalanceApproval) {
    TimecurveObject timecurveObject = new TimecurveObject(null, tenantId, name,
        clearingReference, needBalanceApproval);

    // save TimecurveObject
    try {
      return timecurveObjectRepository.save(timecurveObject);
    } catch (DataAccessException ex) {
      throw timecurveObjectAddException(timecurveObject.getId());
    }
  }

  // only used for manual corrections
  @Transactional
  public TimecurveObject addTimecurve(TimecurveObject timecurveObject) {

    // save TimecurveObject
    try {
      return timecurveObjectRepository.save(timecurveObject);
    } catch (DataAccessException ex) {
      throw timecurveObjectAddException(timecurveObject.getId());
    }
  }

  // only used for manual corrections
  @Transactional
  public void delete(TimecurveObject timecurveObject) {
    try {
      timecurveObjectRepository.delete(timecurveObject);
    } catch (DataAccessException ex) {
      throw timecurveObjectNotFound(timecurveObject.getId().toString());
    }
  }

}
