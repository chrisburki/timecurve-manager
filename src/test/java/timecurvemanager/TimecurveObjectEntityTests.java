package timecurvemanager;


import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.domain.timecurveObject.TimecurveObjectValueType;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntity;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntityRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
public class TimecurveObjectEntityTests {

    private TimecurveObjectEntityRepository entityRepository = Mockito.mock(TimecurveObjectEntityRepository.class);

    @Test
    public void shouldInsertTimecurveEntity() {
        TimecurveObjectEntity entity = new TimecurveObjectEntity("ET1", "EOBJ1", "Object 1", TimecurveObjectValueType.CURRENCY, "CHF", "CHF");
        when(entityRepository.save(any(TimecurveObjectEntity.class))).then(returnsFirstArg());
        TimecurveObjectEntity savedEntity = entityRepository.save(entity);
        assertThat(savedEntity.getTag()).isNotNull();
    }

}
