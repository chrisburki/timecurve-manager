package timecurvemanager.domain.balance;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class ApprovedBalanceSmallerZeroException extends RuntimeException {

  private ApprovedBalanceSmallerZeroException(BigDecimal balance, BigDecimal value) {
    super("Not enough credit balance: " + balance + " + " + value + " < 0");
  }

  public static ApprovedBalanceSmallerZeroException balanceSmallerZero(BigDecimal bigDecimal,
      BigDecimal value) {
    return new ApprovedBalanceSmallerZeroException(bigDecimal, value);
  }
}
