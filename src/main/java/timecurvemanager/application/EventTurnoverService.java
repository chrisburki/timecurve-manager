package timecurvemanager.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.view.EventTurnover;

@Service
@Slf4j
public class EventTurnoverService {

  private final EventItemRepository itemRepository;
  private final EventService eventService;

  public EventTurnoverService(EventItemRepository itemRepository,
      EventService eventService) {
    this.itemRepository = itemRepository;
    this.eventService = eventService;
  }

  private void convertEventItemToTurnover(HashMap<String, EventTurnover> turnoverMap,
      EventItem item) {
    String key =
        item.getTenantId() + ":" + item.getDimension() + ":" + item.getTimecurve().getId() + ":"
            + item.getItemType() + ":" + item.getItemId();
    EventTurnover turnover = turnoverMap.get(key);
    if (turnover == null) {
      turnover = new EventTurnover(item.getTenantId(), item.getDimension(),
          item.getTimecurve().getId(),
          item.getItemType(), item.getItemId(), item.getValue1(),
          eventService.nvl(item.getValue2(), BigDecimal.ZERO),
          eventService.nvl(item.getValue3(), BigDecimal.ZERO), item.getGsn());
    } else {
      // update balance
      BigDecimal newTover1 = new BigDecimal(0)
          .add(turnover.getToverValue1().add(item.getValue1()));
      BigDecimal newTover2 = new BigDecimal(0)
          .add(turnover.getToverValue2().add(eventService.nvl(item.getValue2(),BigDecimal.ZERO)));
      BigDecimal newTover3 = new BigDecimal(0)
          .add(turnover.getToverValue3().add(eventService.nvl(item.getValue3(),BigDecimal.ZERO)));
      turnover.setToverValue1(newTover1);
      turnover.setToverValue2(newTover2);
      turnover.setToverValue3(newTover3);
      turnover.setMaxGsn(Math.max(item.getGsn(), turnover.getMaxGsn()));
    }
    turnoverMap.put(key, turnover);
  }

  public Collection<EventTurnover> getEventTurnoverByGsnRange(Long timecurveId,
      BookKeepingDimension dimension,
      BookKeepingItemType itemType, Long itemId, LocalDate maxDate1, LocalDate maxDate2,
      Long fromGsn, Long toGsn) {
    LocalDate lMaxDate1 = eventService.nvl(maxDate1, eventService.maxDate);
    LocalDate lMaxDate2 = eventService.nvl(maxDate2, eventService.maxDate);

    HashMap<String, EventTurnover> turnoverMap = new HashMap<>();

    List<EventItem> eventItemList = itemRepository.findQueryByTimecurveIdAndGsnBetween(
        timecurveId, dimension, itemType, itemId, lMaxDate1, lMaxDate2, fromGsn, toGsn);

    eventItemList.forEach(item -> convertEventItemToTurnover(turnoverMap, item));
    return turnoverMap.values();
  }
}
