package timecurvemanager.domain.timecurveobject;

import java.util.List;
import java.util.Optional;

public interface TimecurveObjectRepository {

  List<TimecurveObject> findAll();

  Optional<TimecurveObject> findById(Long id);

  List<TimecurveObject> findByName(String name);

  TimecurveObject save(TimecurveObject timecurveObject);

  void delete(TimecurveObject timecurveObject);

}
