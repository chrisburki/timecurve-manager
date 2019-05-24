package timecurvemanager.domain.event;

import timecurvemanager.domain.timecurveObject.TimecurveObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

    List<Event> findAll();

    Optional<Event> findById(Long id);

    Optional<Event> findByEventExtId(Long eventExtId);

    List<Event> findByDimensionAndDate1BetweenAndUseCase(EventDimension dimension, LocalDate fromDate, LocalDate toDate, String useCase);

    List<Event> findByDimensionAndDate2BetweenAndUseCase(EventDimension dimension, LocalDate fromDate, LocalDate toDate, String useCase);

    List<Event> findByDimensionAndDateRange1AndDateRange2AndUseCase(EventDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase);

    Event save(Event event);
}
