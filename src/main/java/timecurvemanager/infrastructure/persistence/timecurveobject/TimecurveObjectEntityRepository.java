package timecurvemanager.infrastructure.persistence.timecurveobject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimecurveObjectEntityRepository extends
    JpaRepository<TimecurveObjectEntity, Long> {

  Optional<TimecurveObjectEntity> findByTag(String tag);

  List<TimecurveObjectEntity> findByName(String label);
}
