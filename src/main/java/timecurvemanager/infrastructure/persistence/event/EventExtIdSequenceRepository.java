package timecurvemanager.infrastructure.persistence.event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventExtIdSequenceRepository extends CrudRepository<EventExtIdSequence, Long> {

}
