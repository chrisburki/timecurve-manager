package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.EventDimension;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {

  Optional<EventEntity> findByEventExtId(Long eventExtId);

  List<EventEntity> findByDimensionAndDate1BetweenAndUseCase(EventDimension dimension,
      LocalDate fromDate, LocalDate toDate, Example<String>  useCase);

  List<EventEntity> findByDimensionAndDate2BetweenAndUseCase(EventDimension dimension,
      LocalDate fromDate, LocalDate toDate, Example<String>  useCase);

  List<EventEntity> findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(
      EventDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, Example<String> useCase);

}
