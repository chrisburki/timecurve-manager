package timecurvemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import timecurvemanager.domain.timecurveObject.TimecurveObject;
import timecurvemanager.domain.timecurveObject.TimecurveObjectRepository;
import timecurvemanager.domain.timecurveObject.TimecurveObjectValueType;

@SpringBootApplication
public class TimecurveManagerApplication {

  private static final Logger log = LoggerFactory.getLogger(TimecurveManagerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TimecurveManagerApplication.class);
  }


  @Bean
  public CommandLineRunner demo(TimecurveObjectRepository repository) {
    return (args) -> {
      // save a couple of timecurves
      repository.save(new TimecurveObject(null, "AAA", "share1chf", "Share 1 CHF",
          TimecurveObjectValueType.CURRENCY, "CHF", null, false));
      repository.save(new TimecurveObject(null, "AAA", "share2eur", "Share 2 EUR",
          TimecurveObjectValueType.CURRENCY, "CHF", null, false));
      repository.save(new TimecurveObject(null, "AAA", "bond1chf", "Bond 1 CHF",
          TimecurveObjectValueType.CURRENCY, "CHF", null, true));
      repository.save(new TimecurveObject(null, "AAA", "macc1eur", "Money Account 1 CHF",
          TimecurveObjectValueType.CURRENCY, "CHF", "CHF", true));
      repository.save(new TimecurveObject(null, "AAA", "macc2chf", "Money Account 2 CHF",
          TimecurveObjectValueType.CURRENCY, "CHF", "CHF", true));

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (TimecurveObject timecurveObject : repository.findAll()) {
        log.info(timecurveObject.toString());
      }
      log.info("");

      // fetch an individual timecurveObject by ID
      repository.findById(1L)
          .ifPresent(timecurve -> {
            log.info("TimecurveObject found with findById(1L):");
            log.info("--------------------------------");
            log.info(timecurve.toString());
            log.info("");
          });

      // fetch timecurves by last name
      log.info("TimecurveObject found with findByName('Share 1 CHF'):");
      log.info("--------------------------------------------");
      repository.findByName("Share 1 CHF").forEach(bauer -> {
        log.info(bauer.toString());
      });
      repository.findByTag("macc1eur").get().toString();
      // for (TimecurveObject macc1 : repository.findByLastName("macc1eur")) {
      // 	log.info(macc1.toString());
      // }
      log.info("");
    };
  }


}
