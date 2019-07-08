package timecurvemanager.infrastructure.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.TimecurveObjectService;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

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
        new ArrayList<>(timecurveObjectService.listObjects()), HttpStatus.OK);
  }

  @GetMapping("/objects/{id}")
  ResponseEntity<TimecurveObject> getObjectById(
      @PathVariable("id") Long id) {
    return new ResponseEntity<>(timecurveObjectService.getById(id), HttpStatus.OK);
  }

  @PostMapping("/objects")
  ResponseEntity<TimecurveObject> createObject(@RequestBody TimecurveObject timecurveObject) {
    TimecurveObject result = timecurveObjectService
        .addTimecurve(timecurveObject);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // readiness probe
  @RequestMapping(value = "/objects/ready", method = RequestMethod.GET)
  ResponseEntity<String> readyObject() {
    return ResponseEntity.status(HttpStatus.OK).body("Service is ready");
  }

}
