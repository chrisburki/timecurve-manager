package timecurvemanager.position.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.position.domain.PositionRepository;
import timecurvemanager.position.domain.model.Position;

@Component
@Slf4j
public class PositionRepositoryImpl implements PositionRepository {

  private final PositionEntityRepository positionEntityRepository;
  private final PositionMapper positionMapper;

  public PositionRepositoryImpl(
      PositionEntityRepository positionEntityRepository,
      PositionMapper positionMapper) {
    this.positionEntityRepository = positionEntityRepository;
    this.positionMapper = positionMapper;
  }

  @Override
  public List<Position> findAll() {
    return positionMapper.mapEntityToDomainList(positionEntityRepository.findAll());
  }

  @Override
  public Optional<Position> findById(Long id) {
    return positionMapper
        .mapOptionalEntityToDomain(positionEntityRepository.findById(id));
  }


  @Override
  public Optional<Position> findByTag(String tag) {
    return positionMapper
        .mapOptionalEntityToDomain(positionEntityRepository.findByTag(tag));
  }

  @Override
  public List<Position> findByContainerId(String containerId) {
    return positionMapper
        .mapEntityToDomainList(positionEntityRepository.findByContainerId(containerId));
  }

  @Override
  public Position save(Position position) {
    return positionMapper.mapEntityToDomain(positionEntityRepository
        .saveAndFlush(positionMapper.mapDomainToEntity(position)));
  }

  @Override
  public void delete(Position position) {
    positionEntityRepository
        .delete(positionMapper.mapDomainToEntity(position));
  }
}
