package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingExtIdSequenceRepository extends CrudRepository<BookingExtIdSequence, Long> {

}
