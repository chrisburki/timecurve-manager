package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import timecurvemanager.bookkeeping.domain.timecurve.ObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;

@Component
@Slf4j
@Profile("int")
public class TimecurveSpiIInt implements TimecurveSpi {

  private String ewomPositionUrl = "http://timecurve-manager:8080/positions/";

  @Override
  public ObjectDetail getObject(String objectId) {
    log.debug("Ask for position with URL: " + ewomPositionUrl + objectId);
    RestTemplate restTemplate = new RestTemplate();

    ExternalObject response = restTemplate.getForObject(
        ewomPositionUrl + objectId, ExternalObject.class);
    ObjectDetail objectDetail = ObjectDetail.builder()
        .objectId(response.getId().toString())
        .tenantId(response.getTenantId())
        .valueType(response.getValueType())
        .valueTag(response.getValueTag())
        .doBalanceCheck(response.getDoBalanceCheck())
        .build();
    return objectDetail;
  }
}
