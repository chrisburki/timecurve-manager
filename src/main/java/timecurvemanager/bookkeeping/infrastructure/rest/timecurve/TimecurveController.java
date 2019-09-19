package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

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
import timecurvemanager.bookkeeping.application.TimecurveService;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;

@RestController
@RequestMapping("/book-keeping")
public class TimecurveController {

  private final TimecurveService timecurveService;

  public TimecurveController(TimecurveService timecurveService
  ) {
    this.timecurveService = timecurveService;
  }

  //Query timecurves
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
    return new ResponseEntity<>(timecurveService.getObjectByTimecuveIdAndDate(id, refDate),
        HttpStatus.OK);
  }

  //Query objects
  @GetMapping("/objects/{id}/timecurves")
  ResponseEntity<List<Timecurve>> getTimecurvesByObjectId(
      @PathVariable("id") String id) {
    return new ResponseEntity<>(
        new ArrayList<>(timecurveService.listTimecuves(id)), HttpStatus.OK);
  }

  @GetMapping("/objects/{id}/timecurve")
  ResponseEntity<Timecurve> getTimecurveByObjectIdAndDate(
      @PathVariable("id") String id,
      @RequestParam(name = "reference-date") @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    return new ResponseEntity<>(timecurveService.getTimecurveByObjectIdAndDate(id, refDate),
        HttpStatus.OK);
  }

  /*
  //Post
  @PostMapping("/objects/{id}/timecurves")
  ResponseEntity<Timecurve> createTimecurve(@RequestBody Timecurve timecurve,
      @PathVariable("id") String id,
      @RequestParam(name = "reference-date", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    return new ResponseEntity<>(timecurveService.createTimecurve(id, timecurve, refDate),
        HttpStatus.OK);
  }
  */

  //Post
  @PostMapping("/objects/{id}/timecurves")
  ResponseEntity<Long> createTimecurve(
      @PathVariable("id") String id,
      @RequestParam(name = "reference-date", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate refDate) {
    return new ResponseEntity<>(timecurveService.createTimecurve(id, refDate),
        HttpStatus.OK);
  }

  // readiness probe
  @RequestMapping(value = "/objects/ready", method = RequestMethod.GET)
  ResponseEntity<String> readyObject() {
    return ResponseEntity.status(HttpStatus.OK).body("Service is ready");
  }

}
