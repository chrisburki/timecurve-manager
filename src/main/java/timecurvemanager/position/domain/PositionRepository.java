package timecurvemanager.position.domain;

import java.util.List;
import java.util.Optional;
import timecurvemanager.position.domain.model.Position;

public interface PositionRepository {

  List<Position> findAll();

  Optional<Position> findById(Long id);

  Optional<Position> findByTag(String tag);

  List<Position> findByContainerId(String name);

  Position save(Position position);

  void delete(Position position);


}
