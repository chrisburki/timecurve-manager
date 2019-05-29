package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Component
public class ApprovedBalanceRepositoryImpl implements ApprovedBalanceRepository {

  private final ApprovedBalanceEntityRepository approvedBalanceRepository;
  private final ApprovedBalanceMapper approvedBalanceMapper;

  public ApprovedBalanceRepositoryImpl(
      ApprovedBalanceEntityRepository approvedBalanceRepository,
      ApprovedBalanceMapper approvedBalanceMapper) {
    this.approvedBalanceRepository = approvedBalanceRepository;
    this.approvedBalanceMapper = approvedBalanceMapper;
  }

  @Override
  public Optional<ApprovedBalance> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId) {
    return approvedBalanceMapper.mapOptionalEntityToDomain(approvedBalanceRepository
        .findByApprovedBalanceEntityIdent(
            new ApprovedBalanceEntityIdent(dimension, timecurveId, itemType, itemId)));
  }

  @Override
  public ApprovedBalance save(ApprovedBalance balance) {
    return approvedBalanceMapper.mapEntityToDomain(approvedBalanceRepository
        .save(approvedBalanceMapper.mapDomainToEntity(balance)));
  }
}
