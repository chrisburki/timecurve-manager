package timecurvemanager.domain.gsn;

public interface GsnRepository {

  Gsn findLast();

  Gsn findBySequenceNr(Long sequenceNr);

  Gsn save(Gsn gsn);
}
