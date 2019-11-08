package timecurvemanager.bookkeeping.infrastructure.rest.booking;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.application.TimecurveService;
import timecurvemanager.bookkeeping.domain.booking.BookingSpi;
import timecurvemanager.bookkeeping.domain.booking.model.BookingTimecurveDetail;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;

@Component
@Slf4j
public class BookingSpiAll implements BookingSpi {


  private final TimecurveService timecurveService;

  public BookingSpiAll(TimecurveService timecurveService) {
    this.timecurveService = timecurveService;
  }

  @Override
  public BookingTimecurveDetail addTimecurve(String objectId, LocalDate refDate) {
    Timecurve timecurve = timecurveService.addTimecurve(objectId, refDate);
    return BookingTimecurveDetail.builder()
        .id(timecurve.getId())
        .objectId(objectId)
        .needClearing(timecurve.needClearing())
        .clearingReference(timecurve.getClearingReference())
        .needBalanceApproval(timecurve.getNeedBalanceApproval())
        .build();
  }

  @Override
  public BookingTimecurveDetail getTimecurve(Long id, LocalDate refDate) {
    Timecurve timecurve = timecurveService.getById(id);
    String objectId = timecurveService.getObjectByTimecuveIdAndDate(id, refDate);
    return BookingTimecurveDetail.builder()
        .id(timecurve.getId())
        .objectId(objectId)
        .needClearing(timecurve.needClearing())
        .clearingReference(timecurve.getClearingReference())
        .needBalanceApproval(timecurve.getNeedBalanceApproval())
        .build();
  }
}
