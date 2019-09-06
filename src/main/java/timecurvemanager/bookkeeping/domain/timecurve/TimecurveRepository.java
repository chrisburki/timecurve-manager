package timecurvemanager.bookkeeping.domain.timecurve;

import java.util.List;
import java.util.Optional;

public interface TimecurveRepository {

  List<Timecurve> findAll();

  Optional<Timecurve> findById(Long id);

  List<Timecurve> findByName(String name);

  Timecurve save(Timecurve timecurve);

  void delete(Timecurve timecurve);

}
