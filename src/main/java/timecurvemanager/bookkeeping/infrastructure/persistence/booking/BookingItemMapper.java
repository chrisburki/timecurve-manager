package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;
import timecurvemanager.bookkeeping.domain.booking.model.BookingItem;

@Component
@Slf4j
public class BookingItemMapper {

  public BookingItemEntity mapDomainToEntity(Booking booking, BookingItem item) {
    return new BookingItemEntity(item.getRowNr(), booking.getTenantId(), booking.getDimension(),
        item.getTimecurveId(), item.getItemType(), item.getItemId(), booking.getDate1(),
        booking.getDate2(), item.getValue1(), item.getValue2(), item.getValue3(), item.getTover1(),
        item.getTover2(), item.getTover3());
  }

  public BookingItem mapEntityToDomain(BookingItemEntity entity) {
    return new BookingItem(entity.getId(),
        entity.getRowNr(), entity.getTimecurveId(),
        entity.getItemType(), entity.getItemId(),
        entity.getValue1(), entity.getValue2(), entity.getValue3(), entity.getTover1(),
        entity.getTover2(), entity.getTover3(), entity.getGsn());
  }

  public List<BookingItem> mapEntityToDomainList(List<BookingItemEntity> entityList) {
    HashMap<Long, Long> bookingMap = new HashMap<>();
    return entityList.stream()
        .map((bookingItemEntity) -> mapEntityToDomain(bookingItemEntity))
        .collect(Collectors.toList());
  }

  public Optional<BookingItem> mapOptionalEntityToDomain(
      Optional<BookingItemEntity> bookingItemEntity) {
    if (bookingItemEntity.isPresent()) {
      BookingItemEntity entity = bookingItemEntity.get();
      return Optional
          .of(new BookingItem(entity.getId(),
              entity.getRowNr(), entity.getTimecurveId(), entity.getItemType(), entity.getItemId(),
              entity.getValue1(), entity.getValue2(), entity.getValue3(), entity.getTover1(),
              entity.getTover2(), entity.getTover3(), entity.getGsn()));
    } else {
      return Optional.empty();
    }
  }

}

