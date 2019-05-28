package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

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
    return Optional.empty();
  }

  @Override
  public ApprovedBalance save(ApprovedBalance balance) {
    return approvedBalanceMapper.mapEntityToDomain(approvedBalanceRepository
        .save(approvedBalanceMapper.mapDomainToEntity(balance)));
  }
}
