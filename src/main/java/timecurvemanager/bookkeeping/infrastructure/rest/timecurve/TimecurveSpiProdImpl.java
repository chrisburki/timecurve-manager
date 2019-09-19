package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import timecurvemanager.bookkeeping.domain.timecurve.ObjectDetail;
import timecurvemanager.bookkeeping.domain.timecurve.TimecurveSpi;
import timecurvemanager.position.domain.PositionValueType;

@Component
@Slf4j
@Profile("prod")
public class TimecurveSpiProdImpl implements TimecurveSpi {

  @Value("${EWOM_POSITION_SERVICE_HOST}")
  private String ewomPositionHost;

  @Value("${{EWOM_POSITION_SERVICE_PORT}")
  private String ewomPositionPort;

  String ewomPositionUrl = "http://" + ewomPositionHost + ":" + ewomPositionPort + "/positions/";

  private class ExternalObject {

    @NotNull
    private Long id;
    @NotNull
    private String tenantId;
    @NotNull
    private String containerId;
    @NotNull
    private String tag;
    @NotNull
    private String name;
    @NotNull
    private PositionValueType valueType;
    @NotNull
    private String valueTag;
    @NotNull
    private Boolean doBalanceCheck;
  }

  @Override
  public ObjectDetail getObject(String objectId) {
    ewomPositionUrl = ewomPositionUrl + objectId;
    RestTemplate restTemplate = new RestTemplate();
    TimecurveSpiProdImpl.ExternalObject externalObject = restTemplate.getForObject(
        ewomPositionUrl, TimecurveSpiProdImpl.ExternalObject.class);
    ObjectDetail objectDetail = ObjectDetail.builder()
        .objectId(externalObject.id.toString())
        .tenantId(externalObject.tenantId)
        .valueType(externalObject.valueType)
        .valueTag(externalObject.valueTag)
        .doBalanceCheck(externalObject.doBalanceCheck).build();
    return objectDetail;
  }

}
