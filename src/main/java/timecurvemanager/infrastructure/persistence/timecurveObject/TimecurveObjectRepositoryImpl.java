package timecurvemanager.infrastructure.persistence.timecurveObject;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.timecurveObject.TimecurveObject;
import timecurvemanager.domain.timecurveObject.TimecurveObjectRepository;

import java.util.List;
import java.util.Optional;

@Component
public class TimecurveObjectRepositoryImpl implements TimecurveObjectRepository {

  private final TimecurveObjectEntityRepository timecurveObjectEntityRepository;
  private final TimecurveObjectMapper timecurveObjectMapper;

  public TimecurveObjectRepositoryImpl(
      TimecurveObjectEntityRepository timecurveObjectEntityRepository,
      TimecurveObjectMapper timecurveObjectMapper/*, TimecurveObjectModelMapper timecurveObjectModelMapper*/) {
    this.timecurveObjectEntityRepository = timecurveObjectEntityRepository;
    this.timecurveObjectMapper = timecurveObjectMapper;
  }

  @Override
  public List<TimecurveObject> findAll() {
    return timecurveObjectMapper.mapEntityToDomainList(timecurveObjectEntityRepository.findAll());
  }

  @Override
  public Optional<TimecurveObject> findById(Long id) {
    return timecurveObjectMapper
        .mapOptionalEntityToDomain(timecurveObjectEntityRepository.findById(id));
  }


  @Override
  public Optional<TimecurveObject> findByTag(String tag) {
    return timecurveObjectMapper
        .mapOptionalEntityToDomain(timecurveObjectEntityRepository.findByTag(tag));
  }

  @Override
  public List<TimecurveObject> findByName(String name) {
    return timecurveObjectMapper
        .mapEntityToDomainList(timecurveObjectEntityRepository.findByName(name));
  }

  @Override
  public TimecurveObject save(TimecurveObject timecurveObject) {
    return timecurveObjectMapper.mapEntityToDomain(timecurveObjectEntityRepository
        .save(timecurveObjectMapper.mapDomainToEntity(timecurveObject)));
  }

  @Override
  public void delete(TimecurveObject timecurveObject) {
    timecurveObjectEntityRepository
        .delete(timecurveObjectMapper.mapDomainToEntity(timecurveObject));
  }

}
