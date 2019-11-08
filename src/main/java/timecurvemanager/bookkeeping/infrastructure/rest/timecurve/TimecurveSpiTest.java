package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;
import timecurvemanager.position.domain.model.PositionValueType;

@Component
@Slf4j
@Profile("test")
public class TimecurveSpiTest implements TimecurveSpi {

  @Override
  public TimecurveObjectDetail getObject(String objectId) {

    final String tenantId = "AAA";
    final PositionValueType valueType = PositionValueType.CURRENCY;
    final String valueTag = "1";
    final Boolean doBalanceCheck = false;


    TimecurveObjectDetail timecurveObjectDetail = TimecurveObjectDetail.builder()
        .objectId(objectId)
        .tenantId(tenantId)
        .valueType(valueType)
        .valueTag(valueTag)
        .doBalanceCheck(doBalanceCheck)
        .build();
    return timecurveObjectDetail;
  }
}
