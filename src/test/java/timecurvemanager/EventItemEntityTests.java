package timecurvemanager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.infrastructure.persistence.event.EventEntity;
import timecurvemanager.infrastructure.persistence.event.EventItemEntity;
import timecurvemanager.infrastructure.persistence.event.EventItemEntityRepository;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntity;

@DataJpaTest
public class EventItemEntityTests {

  private EventItemEntityRepository entityRepository = Mockito
      .mock(EventItemEntityRepository.class);

  // Test Data Event
  private final EventStatus status = EventStatus.OPEN;
  private final Long eventExtId = null;
  private final String useCase = "pay";
  private final Integer seqNr = 1;

  // Test Data TimecurveObject
  private final String name = "Object 1";
  private final String clearingRef = "CHF";
  private final Boolean needBalanceApproval = true;

  // Test Data EventItem
  private final Integer rowNr = 1;
  private final String tenantId = "AAA";
  private final EventDimension dimension = EventDimension.SUBLEDGER;
  private final EventItemType itemType = EventItemType.BASIC;
  private final Long itemId = 1L;
  private final LocalDate date1 = LocalDate.now();
  private final LocalDate date2 = LocalDate.now();
  private final BigDecimal value1 = new BigDecimal(100);
  private final BigDecimal value2 = null;
  private final BigDecimal value3 = null;
  private final BigDecimal tover1 = new BigDecimal(100);
  private final BigDecimal tover2 = null;
  private final BigDecimal tover3 = null;

  @Test
  public void shouldInsertEventItemEntity() {
    EventEntity eventEntity = new EventEntity(eventExtId, seqNr, tenantId, dimension, status,
        useCase, date1, date2);
    TimecurveObjectEntity timecurveEntity = new TimecurveObjectEntity(tenantId, name, clearingRef,
        needBalanceApproval);
    EventItemEntity entity = new EventItemEntity(rowNr, tenantId, dimension, timecurveEntity,
        itemType, itemId, date1, date2, value1, value2, value3, tover1, tover2, tover3);
    when(entityRepository.save(any(EventItemEntity.class))).then(returnsFirstArg());
    EventItemEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getRowNr()).isNotNull();
  }

  @Test
  public void shouldFindEventItemEntityList() {
    EventEntity eventEntity = new EventEntity(eventExtId, seqNr, tenantId, dimension, status,
        useCase, date1, date2);
    TimecurveObjectEntity timecurveEntity = new TimecurveObjectEntity(tenantId, name, clearingRef,
        needBalanceApproval);
    EventItemEntity entity = new EventItemEntity(rowNr, tenantId, dimension, timecurveEntity,
        itemType, itemId, date1, date2, value1, value2, value3, tover1, tover2, tover3);
    List<EventItemEntity> entityList = Arrays.asList(entity);

    // Test
    when(entityRepository
        .findQueryEventItems(
            dimension, timecurveEntity.getId(), itemType, itemId, date1, date1, date2, date2, null))
        .thenReturn(entityList);
    List<EventItemEntity> returnedList = entityRepository
        .findQueryEventItems(
            dimension, timecurveEntity.getId(), itemType, itemId, date1, date1, date2, date2, null);
    assertThat(returnedList.size()).isEqualTo(1);

  }
}
