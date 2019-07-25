package timecurvemanager.infrastructure.persistence.timecurve;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimecurveEntityRepository extends
    JpaRepository<TimecurveEntity, Long> {

  List<TimecurveEntity> findByName(String label);
}
