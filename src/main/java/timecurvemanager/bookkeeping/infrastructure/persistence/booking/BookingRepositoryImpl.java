package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingRepository;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;

@Component
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {

  private final BookingEntityRepository bookingEntityRepository;
  private final BookingMapper bookingMapper;
  private final BookingExtIdSequenceRepository bookingExtIdSequenceRepository;

  public BookingRepositoryImpl(BookingEntityRepository bookingEntityRepository,
      BookingMapper bookingMapper,
      BookingExtIdSequenceRepository bookingExtIdSequenceRepository) {
    this.bookingEntityRepository = bookingEntityRepository;
    this.bookingMapper = bookingMapper;
    this.bookingExtIdSequenceRepository = bookingExtIdSequenceRepository;
  }

  @Override
  public Optional<Booking> findById(Long id) {
    return bookingMapper
        .mapOptionalEntityToDomain(bookingEntityRepository.findById(id));
  }

  @Override
  public Optional<Booking> findLastByBookingExtId(Long bookingExtId) {
    return bookingMapper
        .mapOptionalEntityToDomain(bookingEntityRepository.findLastByBookingExtId(bookingExtId));
  }

  @Override
  public List<Booking> findQueryBookings(BookKeepingDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase, Long maxGsn) {
    return bookingMapper.mapEntityToDomainList(bookingEntityRepository
        .findByFilterCriteria(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase, maxGsn));
  }

  @Override
  public Booking save(Booking booking) {
    return bookingMapper.mapEntityToDomain(bookingEntityRepository
        .save(bookingMapper.mapDomainToEntity(booking)));
  }

  @Override
  public Long getNextBookingExtId() {
    BookingExtIdSequence extIdSequence = new BookingExtIdSequence();
    return bookingExtIdSequenceRepository.save(extIdSequence).getId();
  }

  //items
  @Override
  public Optional<Booking> findQueryByBookingExtId(Long bookingExtId) {
    return bookingMapper
        .mapOptionalEntityWithItemsToDomain(bookingEntityRepository.findQueryByBookingExtId(
            bookingExtId));
  }

  @Override
  public List<Booking> findQueryBookingItems(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase, Long maxGsn) {
    return bookingMapper.mapEntityToDomainList(bookingEntityRepository
        .findQueryBookingItems(dimension, timecurveId, itemType, itemId, fromDate1, toDate1,
            fromDate2, toDate2, useCase, maxGsn));
  }

  @Override
  public List<Booking> findQueryByTimecurveIdAndGsnBetween(Long timecurveId,
      BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId, LocalDate maxDate1,
      LocalDate maxDate2, Long fromGsn, Long toGsn) {
    return bookingMapper.mapEntityToDomainList(
        (bookingEntityRepository
            .findByTimecurveEntityAndGsnBetween(timecurveId, dimension, itemType, itemId, maxDate1,
                maxDate2, fromGsn, toGsn)));
  }

  @Override
  public Long findQueryLastGsnByTimecurve(Long timecurveId,
      BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId) {
    return bookingEntityRepository
        .findQueryLastGsnByTimecurve(timecurveId, dimension, itemType, itemId);
  }
}
