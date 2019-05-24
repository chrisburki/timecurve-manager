package timecurvemanager.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.timecurveObject.TimecurveObject;
import timecurvemanager.domain.timecurveObject.TimecurveObjectRepository;

import java.util.Collection;
import java.util.Optional;

import static timecurvemanager.domain.timecurveObject.TimecurveObjectAddException.timecurveObjectAddException;
import static timecurvemanager.domain.timecurveObject.TimecurveObjectGeneralException.timecurveObjectGeneralException;
import static timecurvemanager.domain.timecurveObject.TimecurveObjectNotCompleteException.timecurveObjectNotComplete;
import static timecurvemanager.domain.timecurveObject.TimecurveObjectNotFoundException.timecurveObjectNotFound;

@Service
public class TimecurveObjectService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String typeTag = "Tag";


  private final TimecurveObjectRepository timecurveObjectRepository;

  public TimecurveObjectService(TimecurveObjectRepository timecurveObjectRepository) {
    this.timecurveObjectRepository = timecurveObjectRepository;
  }

  public Collection<TimecurveObject> listObjects() {
    return timecurveObjectRepository.findAll();
  }

  public TimecurveObject getTimecuve(String anyIdentifier) {
    try {
      long lId = Long.parseLong(anyIdentifier);
      return getById(lId);
    } catch (NumberFormatException | NullPointerException nfe) {
      return getByTag(anyIdentifier);
    }
  }

  public TimecurveObject getById(Long id) {
    return timecurveObjectRepository.findById(id)
        .orElseThrow(() -> timecurveObjectNotFound(id.toString(), primaryKey));

  }

  public TimecurveObject getByTag(String tag) {
    return timecurveObjectRepository.findByTag(tag)
        .orElseThrow(() -> timecurveObjectNotFound(tag, typeTag));

  }

  /*
   * add TimecurveObject
   * *************
   * search for an existing based on the label. if not find create a new object and return it
   * */
  @Transactional
  public TimecurveObject addTimecurve(TimecurveObject timecurveObject) {
    String fieldName;

    // label not null
    if (timecurveObject.getTag() == null) {
      throw timecurveObjectNotComplete(timecurveObject.getId(), typeTag);
    }

    // search for existing timecurveObject and return if existing
    Optional<TimecurveObject> timecurveSearch = timecurveObjectRepository
        .findByTag(timecurveObject.getTag());
    if (timecurveSearch.isPresent()) {
      return timecurveSearch.get();
    }

    // create UUID
//        if (timecurveObject.getObjectId() == null) {
//            timecurveObject.setObjectId(UUID.randomUUID().toString());
//        }

    // check for null
    try {
      fieldName = timecurveObject.validateForNull();
      if (fieldName != null) {
        throw timecurveObjectNotComplete(timecurveObject.getId(), fieldName);
      }
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw timecurveObjectGeneralException(e.getMessage());
    }

    // save timecurveObject
    try {
      return timecurveObjectRepository.save(timecurveObject);
    } catch (DataAccessException ex) {
      throw timecurveObjectAddException(timecurveObject.getId(), timecurveObject.getTag());
    }
  }

  @Transactional
  public void delete(TimecurveObject timecurveObject) {
    try {
      timecurveObjectRepository.delete(timecurveObject);
    } catch (DataAccessException ex) {
      throw timecurveObjectNotFound(timecurveObject.getId().toString(), primaryKey);
    }
  }

}
