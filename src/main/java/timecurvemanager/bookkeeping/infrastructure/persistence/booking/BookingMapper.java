package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;

@Component
@Slf4j
public class BookingMapper {

  private final BookingItemMapper bookingItemMapper;

  public BookingMapper(
      BookingItemMapper bookingItemMapper) {
    this.bookingItemMapper = bookingItemMapper;
  }

  public BookingEntity mapDomainToEntity(Booking booking) {
    BookingEntity entity = new BookingEntity(booking.getBookingExtId(), booking.getSequenceNr(),
        booking.getOrderId(), booking.getTenantId(), booking.getDimension(), booking.getStatus(),
        booking.getUseCase(), booking.getDate1(), booking.getDate2());
    booking.getBookingItems().forEach(item -> {
      entity.addBookingItem(bookingItemMapper.mapDomainToEntity(booking, item));
    });
    return entity;
  }

  public List<BookingEntity> mapDomainToEntityList(List<Booking> bookingList) {
    return bookingList.stream().map(booking -> mapDomainToEntity(booking))
        .collect(Collectors.toList());
  }

  public Booking mapEntityToDomain(BookingEntity entity) {
    Booking booking = new Booking(entity.getId(), entity.getBookingExtId(), entity.getSequenceNr(),
        entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
        entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn());
    entity.getBookingItems().forEach(item -> {
      booking.addBookingItem(bookingItemMapper.mapEntityToDomain(item));
    });
    return booking;
  }

  public Booking mapEntityToDomain2(BookingEntity entity) {
    return new Booking(entity.getId(), entity.getBookingExtId(), entity.getSequenceNr(),
        entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
        entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn());
  }

  public List<Booking> mapEntityToDomainList(List<BookingEntity> entityList) {
    return entityList.stream()
        .map((bookingEntity) -> mapEntityToDomain(bookingEntity))
        .collect(Collectors.toList());
  }

  public Optional<Booking> mapOptionalEntityWithItemsToDomain(
      Optional<BookingEntity> bookingEntity) {
    if (bookingEntity.isPresent()) {
      BookingEntity entity = bookingEntity.get();
      return Optional
          .of(new Booking(entity.getId(), entity.getBookingExtId(), entity.getSequenceNr(),
              entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
              entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn(),
              bookingItemMapper.mapEntityToDomainList(entity.getBookingItems())));
    } else {
      return Optional.empty();
    }
  }

  public Optional<Booking> mapOptionalEntityToDomain(
      Optional<BookingEntity> bookingEntity) {
    if (bookingEntity.isPresent()) {
      BookingEntity entity = bookingEntity.get();
      return Optional
          .of(new Booking(entity.getId(), entity.getBookingExtId(), entity.getSequenceNr(),
              entity.getOrderId(), entity.getTenantId(), entity.getDimension(), entity.getStatus(),
              entity.getUseCase(), entity.getDate1(), entity.getDate2(), entity.getGsn()));
    } else {
      return Optional.empty();
    }
  }

}
