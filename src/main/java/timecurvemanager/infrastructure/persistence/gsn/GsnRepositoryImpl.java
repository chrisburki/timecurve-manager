package timecurvemanager.infrastructure.persistence.gsn;

import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.gsn.Gsn;
import timecurvemanager.domain.gsn.GsnRepository;

@Component
public class GsnRepositoryImpl implements GsnRepository {

  private final GsnEntityRepository entityRepository;
  private final GsnMapper gsnMapper;

  public GsnRepositoryImpl(
      GsnEntityRepository entityRepository,
      GsnMapper gsnMapper) {
    this.entityRepository = entityRepository;
    this.gsnMapper = gsnMapper;
  }

  @Override
  public Gsn findLast() {
    return gsnMapper.mapEntityToDomain(entityRepository.findLast());
  }

  @Override
  public Optional<Gsn> findById(Long id) {
    return gsnMapper.mapOptionalEntityToDomain(entityRepository.findById(id));
  }

  @Override
  public Gsn save(Gsn gsn) {
    return gsnMapper.mapEntityToDomain(entityRepository.save(gsnMapper.mapDomainToEntity(gsn)));
  }
}
