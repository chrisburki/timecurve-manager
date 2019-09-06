package timecurvemanager.bookkeeping.domain.balance;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class ApprovedBalanceChangedException extends RuntimeException {

  private ApprovedBalanceChangedException(BigDecimal oldValue, BigDecimal newValue) {
    super("Old Value: " + oldValue + " not equal new value: " + newValue);
  }

  public static ApprovedBalanceChangedException approvedBalanceChanged(BigDecimal oldValue,
      BigDecimal newValue) {
    return new ApprovedBalanceChangedException(oldValue, newValue);
  }
}
