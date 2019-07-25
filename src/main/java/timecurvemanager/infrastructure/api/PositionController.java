package timecurvemanager.infrastructure.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.PositionService;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionValueType;

@RestController
public class PositionController {

  private final PositionService positionService;

  public PositionController(PositionService positionService) {
    this.positionService = positionService;
  }

  @GetMapping("/positions")
  ResponseEntity<List<Position>> listObjects(
      @RequestParam(name = "container-id", required = false) String containerId) {
    return new ResponseEntity<>(
        new ArrayList<>(positionService.listObjects(containerId)), HttpStatus.OK);
  }

  // @NotNull
  private PositionValueType valueType;

  // @NotNull
  private String valueTag;

  @GetMapping("/positions/tag")
  ResponseEntity<String> getTag(
      @RequestParam(name = "tenant-id") String tenantId,
      @RequestParam(name = "container-id") String containerId,
      @RequestParam(name = "value-type") PositionValueType valueType,
      @RequestParam(name = "value-tag") String valueTag) {
    return new ResponseEntity<>(positionService.getTag(tenantId, containerId, valueType, valueTag),
        HttpStatus.OK);
  }

  @GetMapping("/positions/{id}")
  ResponseEntity<Position> getObjectById(
      @PathVariable("id") Long id) {
    return new ResponseEntity<>(positionService.getById(id), HttpStatus.OK);
  }

  @PostMapping("/positions")
  ResponseEntity<Position> addPosition(@RequestBody Position position) {
    Position result = positionService
        .addPosition(position);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
