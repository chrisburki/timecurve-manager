package timecurvemanager.infrastructure.persistence.position;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionRepository;

@Component
public class PositionObjectRepositoryImpl implements PositionRepository {

  private final PositionEntityRepository positionEntityRepository;
  private final PositionMapper positionMapper;

  public PositionObjectRepositoryImpl(
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
  public List<Position> findByName(String name) {
    return positionMapper
        .mapEntityToDomainList(positionEntityRepository.findByName(name));
  }

  @Override
  public Position save(Position timecurveObject) {
    return positionMapper.mapEntityToDomain(positionEntityRepository
        .saveAndFlush(positionMapper.mapDomainToEntity(timecurveObject)));
  }

  @Override
  public void delete(Position timecurveObject) {
    positionEntityRepository
        .delete(positionMapper.mapDomainToEntity(timecurveObject));
  }
}
