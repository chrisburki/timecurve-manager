package timecurvemanager.gsn.domain;

import java.util.Optional;

public interface GsnRepository {

  Gsn findLast();

  Optional<Gsn> findById(Long id);

  Gsn save(Gsn gsn);
}
