package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.balance.ApprovedBalance;

@Component
public class ApprovedBalanceMapper {

  private final ApprovedBalanceIdentMapper approvedBalanceIdentMapper;

  public ApprovedBalanceMapper(
      ApprovedBalanceIdentMapper approvedBalanceIdentMapper) {
    this.approvedBalanceIdentMapper = approvedBalanceIdentMapper;
  }

  public ApprovedBalanceEntity mapDomainToEntity(ApprovedBalance balance) {
    return new ApprovedBalanceEntity(approvedBalanceIdentMapper
        .mapDomainToEntity(balance.getDimension(), balance.getTimecurveId(), balance.getItemType(),
            balance.getItemId()), balance.getValue1());
  }

  public ApprovedBalance mapEntityToDomain(ApprovedBalanceEntity balance) {
    return new ApprovedBalance(balance.getApprovedBalanceEntityIdent().getDimension(),
        balance.getApprovedBalanceEntityIdent().getTimecurveId(),
        balance.getApprovedBalanceEntityIdent().getItemType(),
        balance.getApprovedBalanceEntityIdent().getItemId(), balance.getValue1());
  }

  public Optional<ApprovedBalance> mapOptionalEntityToDomain(
      Optional<ApprovedBalanceEntity> balanceEntity) {
    if (balanceEntity.isPresent()) {
      ApprovedBalanceEntity balance = balanceEntity.get();
      return Optional.of(new ApprovedBalance(balance.getApprovedBalanceEntityIdent().getDimension(),
          balance.getApprovedBalanceEntityIdent().getTimecurveId(),
          balance.getApprovedBalanceEntityIdent().getItemType(),
          balance.getApprovedBalanceEntityIdent().getItemId(), balance.getValue1()));
    } else {
      return Optional.empty();
    }
  }

}
