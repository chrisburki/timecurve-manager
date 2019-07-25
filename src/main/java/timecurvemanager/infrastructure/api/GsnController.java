package timecurvemanager.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.GsnService;
import timecurvemanager.domain.gsn.Gsn;

@RestController
public class GsnController {

  private final GsnService gsnService;

  public GsnController(GsnService gsnService) {
    this.gsnService = gsnService;
  }

  @GetMapping("/gsns")
  ResponseEntity<Gsn> getlastGsn() {
    return new ResponseEntity<>(gsnService.getLastGsn(),HttpStatus.OK);
  }

  @GetMapping("/gsns/{id}")
  ResponseEntity<Gsn> getByGsnId(
      @PathVariable("id") Long gsnId) {
    return new ResponseEntity<>(gsnService.getGsnById(gsnId),HttpStatus.OK);
  }
}
