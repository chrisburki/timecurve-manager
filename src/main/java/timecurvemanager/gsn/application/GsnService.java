package timecurvemanager.gsn.application;

import static timecurvemanager.gsn.domain.GsnNotFoundException.gsnNotFound;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.gsn.domain.Gsn;
import timecurvemanager.gsn.domain.GsnRepository;

@Service
@Slf4j
public class GsnService {


  private final GsnRepository gsnRepository;

  public GsnService(GsnRepository gsnRepository) {
    this.gsnRepository = gsnRepository;
  }

  /*
   * search GSN
   * **********
   */

  /* Search for existing booking(s) based on id - primary key - probably not needed*/
  public Gsn getGsnById(Long id) {
    return gsnRepository.findById(id).orElseThrow(() -> gsnNotFound(id));
  }

  public Gsn getLastGsn() {
    return gsnRepository.findLast();
  }

  public Gsn getCurrGsn() {
    final Long shift = 100000L;
    LocalDateTime currDateTime = LocalDateTime.now();
    return new Gsn(currDateTime.toLocalDate()
        .getLong(ChronoField.EPOCH_DAY) * shift + currDateTime.getLong(ChronoField.SECOND_OF_DAY),
        currDateTime);
  }

  /*
   * add GSN
   * *******
   */
  public Gsn addGsn() {
    return gsnRepository.save(new Gsn(LocalDateTime.now()));
  }
}
