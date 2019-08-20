package timecurvemanager.infrastructure.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.ObjectTimecurveRelationService;
import timecurvemanager.application.TimecurveService;
import timecurvemanager.domain.timecurve.Timecurve;

@RestController
@RequestMapping("/book-keeping")
public class TimecurveController {

  private final TimecurveService timecurveService;
  private final ObjectTimecurveRelationService relationService;

  public TimecurveController(TimecurveService timecurveService,
      ObjectTimecurveRelationService relationService) {
    this.timecurveService = timecurveService;
    this.relationService = relationService;
  }

  @GetMapping("/timecurves")
  ResponseEntity<List<Timecurve>> listObjects() {
    return new ResponseEntity<>(
        new ArrayList<>(timecurveService.listObjects()), HttpStatus.OK);
  }

  @GetMapping("/timecurves/{id}")
  ResponseEntity<Timecurve> getObjectById(
      @PathVariable("id") Long id) {
    return new ResponseEntity<>(timecurveService.getById(id), HttpStatus.OK);
  }

  @GetMapping("/timecurves/{id}/object")
  ResponseEntity<String> getObjectByTimecurveIdAndDate(
      @PathVariable("id") Long id,
      @RequestParam(name = "reference-date") @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    return new ResponseEntity<>(relationService.getObjectByTimecuveIdAndDate(id, refDate),
        HttpStatus.OK);
  }

  @GetMapping("/objects/{id}/timecurves")
  ResponseEntity<List<Timecurve>> getTimecurvesByObjectId(
      @PathVariable("id") String id) {
    return new ResponseEntity<>(
        new ArrayList<>(relationService.listTimecuves(id)), HttpStatus.OK);
  }

  @GetMapping("/objects/{id}/timecurve")
  ResponseEntity<Timecurve> getTimecurveByObjectIdAndDate(
      @PathVariable("id") String id,
      @RequestParam(name = "reference-date") @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    return new ResponseEntity<>(relationService.getTimecurveByObjectIdAndDate(id, refDate),
        HttpStatus.OK);
  }

  @PostMapping("/objects/{id}/timecurves")
  ResponseEntity<Timecurve> createTimecurve(@PathVariable("id") String id,
      @RequestBody Timecurve timecurve,
      @RequestParam(name = "reference-date", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    Timecurve result = relationService.createTimecurve(id, timecurve, refDate);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  // readiness probe
  @RequestMapping(value = "/objects/ready", method = RequestMethod.GET)
  ResponseEntity<String> readyObject() {
    return ResponseEntity.status(HttpStatus.OK).body("Service is ready");
  }

}
