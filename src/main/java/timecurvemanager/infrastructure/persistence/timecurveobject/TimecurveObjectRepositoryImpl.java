package timecurvemanager.infrastructure.persistence.timecurveobject;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.domain.timecurveobject.TimecurveObjectRepository;

@Component
public class TimecurveObjectRepositoryImpl implements TimecurveObjectRepository {

  private final TimecurveObjectEntityRepository timecurveObjectEntityRepository;
  private final TimecurveObjectMapper timecurveObjectMapper;

  public TimecurveObjectRepositoryImpl(
      TimecurveObjectEntityRepository timecurveObjectEntityRepository,
      TimecurveObjectMapper timecurveObjectMapper) {
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
  public List<TimecurveObject> findByName(String name) {
    return timecurveObjectMapper
        .mapEntityToDomainList(timecurveObjectEntityRepository.findByName(name));
  }

  @Override
  public TimecurveObject save(TimecurveObject timecurveObject) {
    return timecurveObjectMapper.mapEntityToDomain(timecurveObjectEntityRepository
        .saveAndFlush(timecurveObjectMapper.mapDomainToEntity(timecurveObject)));
  }

  @Override
  public void delete(TimecurveObject timecurveObject) {
    timecurveObjectEntityRepository
        .delete(timecurveObjectMapper.mapDomainToEntity(timecurveObject));
  }

}
