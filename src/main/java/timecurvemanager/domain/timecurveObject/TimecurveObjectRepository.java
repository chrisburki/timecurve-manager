package timecurvemanager.domain.timecurveObject;

import java.util.List;
import java.util.Optional;

public interface TimecurveObjectRepository {

    List<TimecurveObject> findAll();

    Optional<TimecurveObject> findById(Long id);

    Optional<TimecurveObject> findByTag(String tag);

    List<TimecurveObject> findByName(String name);

    TimecurveObject save(TimecurveObject timecurveObject);

    void delete(TimecurveObject timecurveObject);

}
