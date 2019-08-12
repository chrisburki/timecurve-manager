package timecurvemanager.application;

import static timecurvemanager.domain.position.PositionAddException.positionAddException;
import static timecurvemanager.domain.position.PositionNotCompleteException.positionNotComplete;
import static timecurvemanager.domain.position.PositionNotFoundException.positionNotFound;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionRepository;
import timecurvemanager.domain.position.PositionValueType;
import timecurvemanager.domain.timecurve.Timecurve;

@Service
@Slf4j
public class PositionService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String typeTag = "Tag";


  private final PositionRepository positionRepository;

  public PositionService(PositionRepository positionRepository) {
    this.positionRepository = positionRepository;
  }

  /*
   * list Positions
   * **************
   * */
  public Collection<Position> listObjects(String containerId) {
    if (containerId == null) {
    return positionRepository.findAll();} else {
      return positionRepository.findByContainerId(containerId);
    }
  }

  public Position getPosition(String anyIdentifier) {
    try {
      Long l = Long.parseLong(anyIdentifier);
      return getById(l);
    } catch (NumberFormatException | NullPointerException nfe) {
      return getByTag(anyIdentifier);
    }
  }

  /*
   * searchPosition
   * **************
   */

  // get Position By Id
  public Position getById(Long id) {
    return positionRepository.findById(id)
        .orElseThrow(() -> positionNotFound(id.toString(), primaryKey));

  }

  // get Position By Tag
  public Position getByTag(String tag) {
    return positionRepository.findByTag(tag)
        .orElseThrow(() -> positionNotFound(tag, typeTag));

  }

  // build Tag
  public String getTag(String tenantId, String containerId, PositionValueType valueType, String valueTag) {
    return tenantId+":"+containerId+":"+valueType.name()+":"+valueTag;

  }


  /*
   * add Position
   * *************
   * search for an existing based on the tag. if not find create a new position and return it
   * */
  @Transactional
  public Position addPosition(Position position) {

    // label not null
    if (position.getTag() == null) {
      throw positionNotComplete(position.getId(), typeTag);
    }

    // search for existing Position and return if existing
    Optional<Position> positionSearch = positionRepository
        .findByTag(position.getTag());
    if (positionSearch.isPresent()) {
      return positionSearch.get();
    }

    // save Position
    try {
      return positionRepository.save(position);
    } catch (DataAccessException ex) {
      throw positionAddException(position.getId(), position.getTag());
    }
  }

  // only for manual corrections
  @Transactional
  public void delete(Position position) {
    try {
      positionRepository.delete(position);
    } catch (DataAccessException ex) {
      throw positionNotFound(position.getId().toString(), primaryKey);
    }
  }

}
