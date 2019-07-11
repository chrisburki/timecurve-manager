package timecurvemanager.application;

import static timecurvemanager.domain.position.PositionAddException.positionAddException;
import static timecurvemanager.domain.position.PositionNotCompleteException.positionNotComplete;
import static timecurvemanager.domain.position.PositionNotFoundException.positionNotFound;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionRepository;
import timecurvemanager.domain.position.PositionValueType;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

//@todo: add testing, testdata for position, timecurve, relations and events

@Service
public class PositionService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String typeTag = "Tag";


  private final PositionRepository positionRepository;
  private final ObjectTimecurveRelationService relationService;

  public PositionService(PositionRepository positionRepository,
      ObjectTimecurveRelationService relationService) {
    this.positionRepository = positionRepository;
    this.relationService = relationService;
  }

  /*
   * list Positions
   * **************
   * */
  public Collection<Position> listObjects() {
    return positionRepository.findAll();
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

  private String getClearingReference(Position position) {
    if (position.getValueType() == PositionValueType.CURRENCY) {
      return position.getValueTag();
    } else {
      return null;
    }
  }

  private Boolean getNeedBalanceApproval(Position position) {
    return position.getDoBalanceCheck() && position.getValueType() == PositionValueType.CURRENCY;
  }

  @Transactional
  public TimecurveObject addTimecurveForPositionAndDate(Position position, LocalDate refDate) {
    // check for existng one, if not exists -> create a new Timecuve Object & Relation to Position
    return relationService
        .addObjectTimecurveRelation(position.getId(), refDate, position.getTenantId(),
            getClearingReference(position), getNeedBalanceApproval(position), "pos").getTimecurve();
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
