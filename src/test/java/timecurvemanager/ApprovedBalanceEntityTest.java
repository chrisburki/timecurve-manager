package timecurvemanager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;
import timecurvemanager.infrastructure.persistence.balance.ApprovedBalanceEntity;
import timecurvemanager.infrastructure.persistence.balance.ApprovedBalanceEntityIdent;
import timecurvemanager.infrastructure.persistence.balance.ApprovedBalanceEntityRepository;

@DataJpaTest
public class ApprovedBalanceEntityTest {

  private ApprovedBalanceEntityRepository balanceRepository = Mockito
      .mock(ApprovedBalanceEntityRepository.class);
  private final BookKeepingDimension dimension = BookKeepingDimension.SUBLEDGER;
  private final Long timecurveId = 1L;
  private final BookKeepingItemType itemType = BookKeepingItemType.BASIC;
  private final Long itemId = 1L;
  private final BigDecimal value1 = new BigDecimal(100);

  @Test
  public void shouldInsertEventItemEntity() {
    ApprovedBalanceEntityIdent balanceEntityIdent = new ApprovedBalanceEntityIdent(dimension,
        timecurveId, itemType, itemId);
    ApprovedBalanceEntity entity = new ApprovedBalanceEntity(null,balanceEntityIdent,
        value1);
    when(balanceRepository.save(any(ApprovedBalanceEntity.class))).then(returnsFirstArg());
    ApprovedBalanceEntity savedEntity = balanceRepository.save(entity);
    assertThat(savedEntity.getApprovedBalanceEntityIdent().getTimecurveId()).isNotNull();
  }

  @Test
  public void shouldFindApprovedBalanceEntity() {
    ApprovedBalanceEntityIdent balanceEntityIdent = new ApprovedBalanceEntityIdent(dimension,
        timecurveId, itemType, itemId);
    ApprovedBalanceEntity entity = new ApprovedBalanceEntity(null, balanceEntityIdent, value1);
    Optional<ApprovedBalanceEntity> entityOptional = Optional.of(entity);
    when(balanceRepository.findByApprovedBalanceEntityIdent(balanceEntityIdent))
        .thenReturn(entityOptional);
    Optional<ApprovedBalanceEntity> returnedEntity = balanceRepository
        .findByApprovedBalanceEntityIdent(balanceEntityIdent);
    assertThat(returnedEntity).isEqualTo(entityOptional);

  }

}
