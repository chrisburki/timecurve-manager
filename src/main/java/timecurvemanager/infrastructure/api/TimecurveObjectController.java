package timecurvemanager.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import timecurvemanager.application.TimecurveObjectService;
import timecurvemanager.domain.timecurveObject.TimecurveObject;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/timecurve")
public class TimecurveObjectController {

  private final TimecurveObjectService timecurveObjectService;

  public TimecurveObjectController(TimecurveObjectService timecurveObjectService) {
    this.timecurveObjectService = timecurveObjectService;
  }

  @GetMapping("/objects")
  ResponseEntity<List<TimecurveObject>> listObjects() {
    return new ResponseEntity<>(
        timecurveObjectService.listObjects().stream().collect(Collectors.toList()), HttpStatus.OK);
  }

  @GetMapping("/objects/{anyIdentifier}")
  ResponseEntity<TimecurveObject> getObjectById(
      @PathVariable("anyIdentifier") String anyIdentifier) {
    return new ResponseEntity<>(timecurveObjectService.getTimecuve(anyIdentifier), HttpStatus.OK);
  }

  @PostMapping("/objects")
  ResponseEntity<TimecurveObject> createObject(@RequestBody TimecurveObject timecurveObject) {
    TimecurveObject result = timecurveObjectService.addTimecurve(timecurveObject);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // readiness probe
  @RequestMapping(value = "/objects/ready", method = RequestMethod.GET)
  ResponseEntity<String> readyObject() {
    return ResponseEntity.status(HttpStatus.OK).body("Service is ready");
  }

}
