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
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;
import timecurvemanager.bookkeeping.infrastructure.persistence.booking.BookingEntity;
import timecurvemanager.bookkeeping.infrastructure.persistence.booking.BookingEntityRepository;

@DataJpaTest
public class BookingEntitryTests {

  private BookingEntityRepository entityRepository = Mockito.mock(BookingEntityRepository.class);

  // Test Data
  private final Long bookingExtId = null;
  private final Integer seqNr = 1;
  private final String orderId = "pay_1";
  private final String tenantId = "AAA";
  private final BookKeepingDimension dimension = BookKeepingDimension.SUBLEDGER;
  private final BookingStatus status = BookingStatus.OPEN;
  private final String useCase = "pay";
  private final LocalDate date1 = LocalDate.now();
  private final LocalDate date2 = LocalDate.now();

  @Test
  public void shouldInsertBookingEntity() {
    BookingEntity entity = new BookingEntity(bookingExtId, seqNr, orderId, tenantId, dimension,
        status,
        useCase, date1, date2);
    when(entityRepository.save(any(BookingEntity.class))).then(returnsFirstArg());
    BookingEntity savedEntity = entityRepository.save(entity);
    assertThat(savedEntity.getTenantId()).isNotNull();
  }

  @Test
  public void shouldFindBookingEntityList() {
    BookingEntity entity = new BookingEntity(bookingExtId, seqNr, orderId, tenantId, dimension,
        status,
        useCase, date1, date2);
    List<BookingEntity> entityList = Arrays.asList(entity);

    // Test 1
    when(entityRepository
        .findByFilterCriteria(BookKeepingDimension.SUBLEDGER, date1, date2, date1, date2, useCase,
            null))
        .thenReturn(entityList);
    List<BookingEntity> returnedList4 = entityRepository
        .findByFilterCriteria(BookKeepingDimension.SUBLEDGER, date1, date2, date1, date2, useCase,
            null);
    assertThat(returnedList4.size()).isEqualTo(1);

  }
}
