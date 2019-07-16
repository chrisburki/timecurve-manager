package timecurvemanager.domain.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

  Optional<Event> findById(Long id);

  Optional<Event> findLastByEventExtId(Long eventExtId);

  List<Event> findQueryEvents(
      EventDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase);

  Event save(Event event);

  /*internal*/
  Long getNextEventExtId();

}
