package timecurvemanager.infrastructure.persistence.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "event_ext_seq", initialValue = 1, allocationSize = 1)
public class EventExtIdSequence {

  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_ext_seq")
  @Id
  Long id;
}