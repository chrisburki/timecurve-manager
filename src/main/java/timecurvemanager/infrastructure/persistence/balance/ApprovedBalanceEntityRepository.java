package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Repository
public interface ApprovedBalanceEntityRepository extends JpaRepository<ApprovedBalanceEntity, Long> {

  Optional<ApprovedBalanceEntity> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      EventDimension dimension,
      Long timecurveId, EventItemType itemType, Long itemId);

}
