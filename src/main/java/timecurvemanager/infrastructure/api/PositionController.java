package timecurvemanager.infrastructure.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.PositionService;
import timecurvemanager.domain.position.Position;

@RestController
public class PositionController {

  private final PositionService positionService;

  public PositionController(PositionService positionService) {
    this.positionService = positionService;
  }

  @GetMapping("/objects")
  ResponseEntity<List<Position>> listObjects() {
    return new ResponseEntity<>(
        new ArrayList<>(positionService.listObjects()), HttpStatus.OK);
  }

  @GetMapping("/objects/{id}")
  ResponseEntity<Position> getObjectById(
      @PathVariable("id") Long id) {
    return new ResponseEntity<>(positionService.getById(id), HttpStatus.OK);
  }

  @PostMapping("/objects")
  ResponseEntity<Position> createPosition(@RequestBody Position position) {
    Position result = positionService
        .addPosition(position);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
