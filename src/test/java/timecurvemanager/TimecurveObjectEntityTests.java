package timecurvemanager;


import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.domain.timecurveObject.TimecurveObjectValueType;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntity;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
public class TimecurveObjectEntityTests {

  private TimecurveObjectEntityRepository entityRepository = Mockito
      .mock(TimecurveObjectEntityRepository.class);

  // Test Data
  private final String tenantId = "AAA";
  private final String tag = "TAG1";
  private final String name = "Object 1";
  private final TimecurveObjectValueType objectValueType = TimecurveObjectValueType.CURRENCY;
  private final String valueTag = "CHF";
  private final String clearingRef = "CHF";
  private final Boolean needBalanceApproval = true;


  @Test
  public void shouldInsertTimecurveEntity() {
    TimecurveObjectEntity entity = new TimecurveObjectEntity(tenantId, tag, name, objectValueType,
        valueTag, clearingRef, needBalanceApproval);
    when(entityRepository.save(any(TimecurveObjectEntity.class))).then(returnsFirstArg());
    TimecurveObjectEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getTag()).isNotNull();
  }

  @Test
  public void shouldFindTimecurveEntity() {
    TimecurveObjectEntity entity = new TimecurveObjectEntity(tenantId, tag, name, objectValueType,
        valueTag, clearingRef, needBalanceApproval);
    List<TimecurveObjectEntity> entityList = Arrays.asList(entity);
    when(entityRepository.findByName("Object 1")).thenReturn(entityList);
    List<TimecurveObjectEntity> returnedEntity1 = entityRepository.findByName(name);
    assertThat(returnedEntity1.size()).isEqualTo(1);

    Optional<TimecurveObjectEntity> entityOptional = Optional.of(entity);
    when(entityRepository.findByTag(tag)).thenReturn(entityOptional);
    Optional<TimecurveObjectEntity> returnedEntity2 = entityRepository.findByTag(tag);
    assertThat(returnedEntity2).isEqualTo(entityOptional);
  }

}
