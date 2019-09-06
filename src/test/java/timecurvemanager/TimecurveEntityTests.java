package timecurvemanager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.bookkeeping.infrastructure.persistence.timecurve.TimecurveEntity;
import timecurvemanager.bookkeeping.infrastructure.persistence.timecurve.TimecurveEntityRepository;

@DataJpaTest
public class TimecurveEntityTests {

  private TimecurveEntityRepository entityRepository = Mockito
      .mock(TimecurveEntityRepository.class);

  // Test Data
  private final String tenantId = "AAA";
  private final String name = "Object 1";
  private final String clearingRef = "CHF";
  private final Boolean needBalanceApproval = true;


  @Test
  public void shouldInsertTimecurveEntity() {
    TimecurveEntity entity = new TimecurveEntity(tenantId, name, clearingRef,
        needBalanceApproval);
    when(entityRepository.save(any(TimecurveEntity.class))).then(returnsFirstArg());
    TimecurveEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getTenantId()).isNotNull();
  }

  @Test
  public void shouldFindTimecurveEntity() {
    TimecurveEntity entity = new TimecurveEntity(tenantId, name, clearingRef,
        needBalanceApproval);
    List<TimecurveEntity> entityList = Arrays.asList(entity);
    when(entityRepository.findByName("Object 1")).thenReturn(entityList);
    List<TimecurveEntity> returnedEntity1 = entityRepository.findByName(name);
    assertThat(returnedEntity1.size()).isEqualTo(1);

  }

}
