package timecurvemanager.domain.position;

import java.util.List;
import java.util.Optional;

public interface PositionRepository {

  List<Position> findAll();

  Optional<Position> findById(Long id);

  Optional<Position> findByTag(String tag);

  List<Position> findByName(String name);

  Position save(Position timecurveObject);

  void delete(Position timecurveObject);


}
