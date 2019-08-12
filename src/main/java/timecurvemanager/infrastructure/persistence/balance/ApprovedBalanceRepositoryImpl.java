package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;

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
  public Optional<ApprovedBalance> findById(Long id) {
    return approvedBalanceMapper.mapOptionalEntityToDomain(approvedBalanceRepository.findById(id));
  }

  @Override
  public Optional<ApprovedBalance> findByDimensionAndTimecurveIdAndItemTypeAndItemId(
      BookKeepingDimension dimension, Long timecurveId, BookKeepingItemType itemType, Long itemId) {
    return approvedBalanceMapper.mapOptionalEntityToDomain(approvedBalanceRepository
        .findByApprovedBalanceEntityIdent(
            new ApprovedBalanceEntityIdent(dimension, timecurveId, itemType, itemId)));
  }

  @Override
  public ApprovedBalance save(ApprovedBalance balance) {
    return approvedBalanceMapper.mapEntityToDomain(approvedBalanceRepository
        .saveAndFlush(approvedBalanceMapper.mapDomainToEntity(balance)));
  }

}
