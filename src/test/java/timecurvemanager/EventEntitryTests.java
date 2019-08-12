package timecurvemanager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.infrastructure.persistence.event.EventEntity;
import timecurvemanager.infrastructure.persistence.event.EventEntityRepository;

@DataJpaTest
public class EventEntitryTests {

  private EventEntityRepository entityRepository = Mockito.mock(EventEntityRepository.class);

  // Test Data
  private final Long eventExtId = null;
  private final Integer seqNr = 1;
  private final String orderId = "pay_1";
  private final String tenantId = "AAA";
  private final BookKeepingDimension dimension = BookKeepingDimension.SUBLEDGER;
  private final EventStatus status = EventStatus.OPEN;
  private final String useCase = "pay";
  private final LocalDate date1 = LocalDate.now();
  private final LocalDate date2 = LocalDate.now();

  @Test
  public void shouldInsertEventEntity() {
    EventEntity entity = new EventEntity(eventExtId, seqNr, orderId, tenantId, dimension, status,
        useCase, date1, date2);
    when(entityRepository.save(any(EventEntity.class))).then(returnsFirstArg());
    EventEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getTenantId()).isNotNull();
  }

  @Test
  public void shouldFindEventEntityList() {
    EventEntity entity = new EventEntity(eventExtId, seqNr, orderId, tenantId, dimension, status,
        useCase, date1, date2);
    List<EventEntity> entityList = Arrays.asList(entity);

    // Test 1
    when(entityRepository
        .findByFilterCriteria(BookKeepingDimension.SUBLEDGER, date1, date2, date1, date2, useCase))
        .thenReturn(entityList);
    List<EventEntity> returnedList4 = entityRepository
        .findByFilterCriteria(BookKeepingDimension.SUBLEDGER, date1, date2, date1, date2, useCase);
    assertThat(returnedList4.size()).isEqualTo(1);

  }
}
