package timecurvemanager.application;

import static timecurvemanager.domain.timecurveobject.TimecurveObjectAddException.timecurveObjectAddException;
import static timecurvemanager.domain.timecurveobject.TimecurveObjectNotCompleteException.timecurveObjectNotComplete;
import static timecurvemanager.domain.timecurveobject.TimecurveObjectNotFoundException.timecurveObjectNotFound;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.domain.timecurveobject.TimecurveObjectRepository;

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
      Long l = Long.parseLong(anyIdentifier);
      return getById(l);
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

    // search for existing TimecurveObject and return if existing
    Optional<TimecurveObject> timecurveSearch = timecurveObjectRepository
        .findByTag(timecurveObject.getTag());
    if (timecurveSearch.isPresent()) {
      return timecurveSearch.get();
    }

    // create UUID
    // if (TimecurveObject.getObjectId() == null) {
    //   TimecurveObject.setObjectId(UUID.randomUUID().toString());
    // }

    // check for null (not working)
    /*try {
      fieldName = timecurveObject.validateForNull(timecurveObject);
      if (fieldName != null) {
        throw timecurveObjectNotComplete(timecurveObject.getId(), fieldName);
      }
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw timecurveObjectGeneralException(e.getMessage());
    }*/

    // save TimecurveObject
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
