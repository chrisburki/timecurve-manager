package timecurvemanager.infrastructure.persistence.position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.position.Position;

@Component
public class PositionMapper {

  public PositionEntity mapDomainToEntity(Position position) {
    return new PositionEntity(position.getTenantId(), position.getContainerId(), position.getTag(),
        position.getName(), position.getValueType(), position.getValueTag(),
        position.getDoBalanceCheck());
  }

  public List<PositionEntity> mapDomainToEntityList(List<Position> positionList) {
    return positionList.stream().map((position) -> mapDomainToEntity(position))
        .collect(Collectors.toList());
  }

  public Position mapEntityToDomain(PositionEntity entity) {
    return new Position(entity.getId(), entity.getTenantId(), entity.getContainerId(),
        entity.getTag(),
        entity.getName(), entity.getValueType(), entity.getValueTag(), entity.getDoBalanceCheck());
  }

  public List<Position> mapEntityToDomainList(List<PositionEntity> entityList) {
    return entityList.stream()
        .map((positionEntity) -> mapEntityToDomain(positionEntity))
        .collect(Collectors.toList());
  }

  public Optional<Position> mapOptionalEntityToDomain(
      Optional<PositionEntity> positionEntity) {
    if (positionEntity.isPresent()) {
      PositionEntity entity = positionEntity.get();
      return Optional.of(new Position(entity.getId(), entity.getTenantId(), entity.getContainerId(),
          entity.getTag(), entity.getName(), entity.getValueType(), entity.getValueTag(),
          entity.getDoBalanceCheck()));
    } else {
      return Optional.empty();
    }
  }

}
