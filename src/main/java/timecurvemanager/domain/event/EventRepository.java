package timecurvemanager.domain.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.Event;

public interface EventRepository {

  Optional<Event> findById(Long id);

  Optional<Event> findLastByEventExtId(Long eventExtId);

  List<Event> findQueryEvents(
      BookKeepingDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase);

  Event save(Event event);

  /*internal*/
  Long getNextEventExtId();

}
