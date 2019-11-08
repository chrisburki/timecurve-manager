package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;

@Component
@Slf4j
@Profile("dev")
public class TimecurveSpiDev implements TimecurveSpi {

  private String ewomPositionUrl = "http://localhost:8080/positions/";

  @Override
  public TimecurveObjectDetail getObject(String objectId) {
    log.debug("Ask for position with URL: " + ewomPositionUrl + objectId);
    RestTemplate restTemplate = new RestTemplate();

    TimecurveObjectExternal response = restTemplate.getForObject(
        ewomPositionUrl + objectId, TimecurveObjectExternal.class);
    TimecurveObjectDetail timecurveObjectDetail = TimecurveObjectDetail.builder()
        .objectId(response.getId().toString())
        .tenantId(response.getTenantId())
        .valueType(response.getValueType())
        .valueTag(response.getValueTag())
        .doBalanceCheck(response.getDoBalanceCheck())
        .build();
    return timecurveObjectDetail;
  }
}
