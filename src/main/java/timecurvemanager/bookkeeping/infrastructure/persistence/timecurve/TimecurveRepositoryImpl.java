package timecurvemanager.bookkeeping.infrastructure.persistence.timecurve;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveRepository;

@Component
@Slf4j
public class TimecurveRepositoryImpl implements TimecurveRepository {

  private final TimecurveEntityRepository timecurveEntityRepository;
  private final TimecurveMapper timecurveMapper;

  public TimecurveRepositoryImpl(
      TimecurveEntityRepository timecurveEntityRepository,
      TimecurveMapper timecurveMapper) {
    this.timecurveEntityRepository = timecurveEntityRepository;
    this.timecurveMapper = timecurveMapper;
  }

  @Override
  public List<Timecurve> findAll() {
    return timecurveMapper.mapEntityToDomainList(timecurveEntityRepository.findAll());
  }

  @Override
  public Optional<Timecurve> findById(Long id) {
    return timecurveMapper
        .mapOptionalEntityToDomain(timecurveEntityRepository.findById(id));
  }

  @Override
  public List<Timecurve> findByName(String name) {
    return timecurveMapper
        .mapEntityToDomainList(timecurveEntityRepository.findByName(name));
  }

  @Override
  public Timecurve save(Timecurve timecurve) {
    return timecurveMapper.mapEntityToDomain(timecurveEntityRepository
        .saveAndFlush(timecurveMapper.mapDomainToEntity(timecurve)));
  }

  @Override
  public void delete(Timecurve timecurve) {
    timecurveEntityRepository
        .delete(timecurveMapper.mapDomainToEntity(timecurve));
  }

}
