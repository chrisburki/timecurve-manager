package timecurvemanager.infrastructure.persistence.timecurveObject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimecurveObjectEntityRepository extends JpaRepository<TimecurveObjectEntity, Long> {

    Optional<TimecurveObjectEntity> findByTag(String tag);

    List<TimecurveObjectEntity> findByName(String label);
}
