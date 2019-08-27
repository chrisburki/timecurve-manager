package timecurvemanager.application;

import static timecurvemanager.domain.timecurve.TimecurveAddException.timecurveAddException;
import static timecurvemanager.domain.timecurve.TimecurveNotFoundException.timecurveNotFound;

import java.sql.Time;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.timecurve.Timecurve;
import timecurvemanager.domain.timecurve.TimecurveRepository;

@Service
@Slf4j
public class TimecurveService {

  private final TimecurveRepository timecurveRepository;

  public TimecurveService(TimecurveRepository timecurveRepository) {
    this.timecurveRepository = timecurveRepository;
  }

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
   * add Timecurve for an object
   * *************
   * search for an existing based on the label. if not find create a new object and return it
   * */
  public Timecurve addTimecurve(String tenantId, String name, String clearingReference,
      Boolean needBalanceApproval) {
    Timecurve timecurve = new Timecurve(null, tenantId, name,
        clearingReference, needBalanceApproval);

    // save Timecurve
    try {
      return timecurveRepository.save(timecurve);
    } catch (DataAccessException ex) {
      throw timecurveAddException(timecurve.getId());
    }
  }

  // only used for manual corrections
  @Transactional
  public Timecurve addTimecurve(Timecurve timecurve) {

    // save Timecurve
    try {
      return timecurveRepository.save(timecurve);
    } catch (DataAccessException ex) {
      throw timecurveAddException(timecurve.getId());
    }
  }

  // only used for manual corrections
  @Transactional
  public void delete(Timecurve timecurve) {
    try {
      timecurveRepository.delete(timecurve);
    } catch (DataAccessException ex) {
      throw timecurveNotFound(timecurve.getId().toString());
    }
  }

}
