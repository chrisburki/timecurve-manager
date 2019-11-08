package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;
import timecurvemanager.position.application.PositionService;
import timecurvemanager.position.domain.model.Position;

@Component
@Slf4j
@Profile("prod")
public class TimecurveSpiProd implements TimecurveSpi {

  private final PositionService positionService;

  public TimecurveSpiProd(PositionService positionService) {
    this.positionService = positionService;
  }

  @Override
  public TimecurveObjectDetail getObject(String objectId) {
    Position objectExternal = positionService.getPosition(objectId);
    TimecurveObjectDetail timecurveObjectDetail = TimecurveObjectDetail.builder()
        .objectId(objectExternal.getId().toString())
        .tenantId(objectExternal.getTenantId())
        .valueType(objectExternal.getValueType())
        .valueTag(objectExternal.getValueTag())
        .doBalanceCheck(objectExternal.getDoBalanceCheck())
        .build();
    return timecurveObjectDetail;
  }

  /*
  @Value("${EWOM_POSITION_SERVICE_HOST}")
  private String ewomPositionHost;

  @Value("${{EWOM_POSITION_SERVICE_PORT}")
  private String ewomPositionPort;

  String ewomPositionUrl = "http://" + ewomPositionHost + ":" + ewomPositionPort + "/positions/";

  @Override
  public TimecurveObjectDetail getObject(String objectId) {
    ewomPositionUrl = ewomPositionUrl + objectId;
    RestTemplate restTemplate = new RestTemplate();
    TimecurveObjectExternal response = restTemplate.getForObject(
        ewomPositionUrl, TimecurveObjectExternal.class);
    TimecurveObjectDetail timecurveObjectDetail = TimecurveObjectDetail.builder()
        .objectId(response.getId().toString())
        .tenantId(response.getTenantId())
        .valueType(response.getValueType())
        .valueTag(response.getValueTag())
        .doBalanceCheck(response.getDoBalanceCheck())
        .build();
    return timecurveObjectDetail;
  }
   */


}
