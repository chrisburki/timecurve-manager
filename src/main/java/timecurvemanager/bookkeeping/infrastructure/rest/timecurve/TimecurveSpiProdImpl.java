package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import timecurvemanager.bookkeeping.domain.timecurve.ObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;

@Component
@Slf4j
@Profile("prod")
public class TimecurveSpiProdImpl implements TimecurveSpi {

  @Value("${EWOM_POSITION_SERVICE_HOST}")
  private String ewomPositionHost;

  @Value("${{EWOM_POSITION_SERVICE_PORT}")
  private String ewomPositionPort;

  String ewomPositionUrl = "http://" + ewomPositionHost + ":" + ewomPositionPort + "/positions/";

  @Override
  public ObjectDetail getObject(String objectId) {
    ewomPositionUrl = ewomPositionUrl + objectId;
    RestTemplate restTemplate = new RestTemplate();
    ExternalObject response = restTemplate.getForObject(
        ewomPositionUrl, ExternalObject.class);
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
