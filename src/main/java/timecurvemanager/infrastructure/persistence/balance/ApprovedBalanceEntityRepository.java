package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovedBalanceEntityRepository extends
    JpaRepository<ApprovedBalanceEntity, ApprovedBalanceEntityIdent> {

  Optional<ApprovedBalanceEntity> findByApprovedBalanceEntityIdent(
      ApprovedBalanceEntityIdent approvedBalanceEntityIdent);

  // @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ApprovedBalanceEntity> findById(Long id);

}
