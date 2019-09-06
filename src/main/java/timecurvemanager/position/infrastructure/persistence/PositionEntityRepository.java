package timecurvemanager.position.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionEntityRepository extends
    JpaRepository<PositionEntity, Long> {

  Optional<PositionEntity> findByTag(String tag);

  List<PositionEntity> findByContainerId(String containerId);

}
