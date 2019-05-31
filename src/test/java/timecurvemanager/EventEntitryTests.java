package timecurvemanager;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.infrastructure.persistence.event.EventEntity;
import timecurvemanager.infrastructure.persistence.event.EventEntityRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
public class EventEntitryTests {

  private EventEntityRepository entityRepository = Mockito.mock(EventEntityRepository.class);

  // Test Data
  private final Long eventExtId = 2L;
  private final Integer seqNr = 1;
  private final String tenantId = "AAA";
  private final EventDimension dimension = EventDimension.SUBLEDGER;
  private final EventStatus status = EventStatus.OPEN;
  private final String useCase = "pay";
  private final LocalDate date1 = LocalDate.now();
  private final LocalDate date2 = LocalDate.now();

  @Test
  public void shouldInsertEventEntity() {
    EventEntity entity = new EventEntity(eventExtId, seqNr, tenantId, dimension, status, useCase,
        date1, date2);
    when(entityRepository.save(any(EventEntity.class))).then(returnsFirstArg());
    EventEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getEventExtId()).isNotNull();
  }

  @Test
  public void shouldFindEventEntityList() {
    EventEntity entity = new EventEntity(eventExtId, seqNr, tenantId, dimension, status, useCase,
        date1, date2);
    List<EventEntity> entityList = Arrays.asList(entity);

    // Test 1
    when(entityRepository
        .findByDimensionAndDate1BetweenAndUseCase(EventDimension.SUBLEDGER, date1, date2, Example.of("pay")))
        .thenReturn(entityList);
    List<EventEntity> returnedList1 = entityRepository
        .findByDimensionAndDate1BetweenAndUseCase(EventDimension.SUBLEDGER, date1, date2, Example.of("pay"));
    assertThat(returnedList1.size()).isEqualTo(1);

    // Test 2
    when(entityRepository
        .findByDimensionAndDate2BetweenAndUseCase(EventDimension.SUBLEDGER, date1, date2, Example.of("pay")))
        .thenReturn(entityList);
    List<EventEntity> returnedList2 = entityRepository
        .findByDimensionAndDate1BetweenAndUseCase(EventDimension.SUBLEDGER, date1, date2, Example.of("pay"));
    assertThat(returnedList2.size()).isEqualTo(1);

    when(entityRepository
        .findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(EventDimension.SUBLEDGER, date1,
            date2, date1, date2, Example.of("pay"))).thenReturn(entityList);

    // Test 3
    List<EventEntity> returnedList3 = entityRepository
        .findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(EventDimension.SUBLEDGER, date1,
            date2, date1, date2, Example.of("pay"));
    assertThat(returnedList3.size()).isEqualTo(1);
  }
}
