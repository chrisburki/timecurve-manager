package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(name = "booking_ext_seq", initialValue = 1, allocationSize = 1)
@NoArgsConstructor
@Getter
public class BookingExtIdSequence {

  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_ext_seq")
  @Id
  Long id;
}