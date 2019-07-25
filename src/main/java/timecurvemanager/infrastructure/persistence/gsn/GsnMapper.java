package timecurvemanager.infrastructure.persistence.gsn;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.gsn.Gsn;

@Component
@Slf4j
public class GsnMapper {

  public GsnEntity mapDomainToEntity(Gsn gsn) {
    return new GsnEntity(gsn.getGsnDate());
  }

  public Optional<Gsn> mapOptionalEntityToDomain(Optional<GsnEntity> entity) {
    if (entity.isPresent()) {
      GsnEntity gsnEntity = entity.get();
      return Optional.of(new Gsn(gsnEntity.getId(), gsnEntity.gsnDate));
    } else {
      return Optional.empty();
    }
  }

  public Gsn mapEntityToDomain(GsnEntity entity) {
    return new Gsn(entity.getId(), entity.gsnDate);
  }
}
