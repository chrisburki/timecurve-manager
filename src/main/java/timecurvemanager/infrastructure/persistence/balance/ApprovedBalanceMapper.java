package timecurvemanager.infrastructure.persistence.balance;

import java.util.Optional;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.balance.ApprovedBalance;

@Component
public class ApprovedBalanceMapper {

  public ApprovedBalanceEntity mapDomainToEntity(ApprovedBalance balance) {
    return new ApprovedBalanceEntity(balance.getDimension(), balance.getTimecurveId(), balance.getItemType(), balance.getItemId(), balance.getValue1());
  }

  public ApprovedBalance mapEntityToDomain(ApprovedBalanceEntity balance) {
    return new ApprovedBalance(balance.getId(), balance.getDimension(), balance.getTimecurveId(), balance.getItemType(), balance.getItemId(), balance.getValue1());
  }

  public Optional<ApprovedBalance> mapOptionalEntityToDomain(
      Optional<ApprovedBalanceEntity> balanceEntity) {
    if (balanceEntity.isPresent()) {
      ApprovedBalanceEntity balance = balanceEntity.get();
      return Optional.of(new ApprovedBalance(balance.getId(), balance.getDimension(), balance.getTimecurveId(), balance.getItemType(), balance.getItemId(), balance.getValue1()));
    } else {
      return Optional.empty();
    }
  }
}
