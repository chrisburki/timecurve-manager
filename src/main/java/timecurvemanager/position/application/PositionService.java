package timecurvemanager.position.application;

import static timecurvemanager.position.domain.model.PositionAddException.positionAddException;
import static timecurvemanager.position.domain.model.PositionNotCompleteException.positionNotComplete;
import static timecurvemanager.position.domain.model.PositionNotFoundException.positionNotFound;

import java.util.Collection;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.position.domain.api.PositionCommand;
import timecurvemanager.position.domain.api.PositionExternalEvent;
import timecurvemanager.position.domain.model.Position;
import timecurvemanager.position.domain.PositionRepository;
import timecurvemanager.position.domain.model.PositionValueType;

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
      return positionRepository.findAll();
    } else {
      return positionRepository.findByContainerId(containerId);
    }
  }

  /*
   * searchPosition
   * **************
   */
  // get Position By Id
  private Position getById(Long id) {
    return positionRepository.findById(id)
        .orElseThrow(() -> positionNotFound(id.toString(), primaryKey));

  }

  // get Position By Tag
  private Position getByTag(String tag) {
    return positionRepository.findByTag(tag)
        .orElseThrow(() -> positionNotFound(tag, typeTag));

  }

  public Position getPosition(String anyIdentifier) {
    try {
      Long l = Long.parseLong(anyIdentifier);
      return getById(l);
    } catch (NumberFormatException | NullPointerException nfe) {
      return getByTag(anyIdentifier);
    }
  }

  // build Tag
  public String getTag(String tenantId, String containerId, PositionValueType valueType,
      String valueTag) {
    String addon = "";
    switch (valueType) {
      case CURRENCY:
        addon = "#INT";
        break;
      case SECURITY:
        addon = "#SEC";
        break;
    }
    return tenantId + "#" + containerId + "#" + valueType.name() + "#" + valueTag + addon;
  }

  // calculate name
  private String calcName(String valueTag, PositionValueType valueType, String containerId) {
    String type = "";
    if (valueType == PositionValueType.CURRENCY) {
      switch (containerId.charAt(0)) {
        case 'C':
          type = "Money Account";
          break;
        default:
          type = "Interim Position";
      }
    } else {
      type = "Security Position";
    }
    return valueTag + ": " + type + " for Container " + containerId;
  }

  // calculate doBalanceCheck
  private Boolean calcDoBalanceCheck(PositionValueType valueType, String containerId) {
    return valueType == PositionValueType.CURRENCY && containerId.charAt(0) == 'C';
  }

  /*
   * add Position
   * *************
   * search for an existing based on the tag. if not find create a new position and return it
   * */
  @Transactional
  private Position addPosition(Position position) {

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

  public PositionExternalEvent processPositionCommand(PositionCommand positionCommand) {
    String tag;
    if (positionCommand.getTag() == null) {
      tag = getTag(positionCommand.getTenantId(), positionCommand.getContainerId(),
          positionCommand.getValueType(), positionCommand.getValueTag());
    } else {
      tag = positionCommand.getTag();
    }
    Position position = new Position(
        positionCommand.getTenantId(),
        positionCommand.getContainerId(),
        tag,
        calcName(positionCommand.getValueTag(), positionCommand.getValueType(),
            positionCommand.getContainerId()),
        positionCommand.getValueType(),
        positionCommand.getValueTag(),
        calcDoBalanceCheck(positionCommand.getValueType(), positionCommand.getContainerId()));
    position = addPosition(position);
    return PositionExternalEvent.builder()
        .id(position.getId())
        .tag(position.getTag())
        .name(position.getName())
        .needBalanceCheck(position.getDoBalanceCheck()).build();
  }

}
