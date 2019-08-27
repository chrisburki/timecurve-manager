package timecurvemanager.domain.event.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.EventStatus;

@Getter
@Builder
@ToString
public class BookingMessage implements Serializable {

  private final Long eventExtId;

  private final  Integer sequenceNr;

  @NotNull
  private final  String orderId;

  @NotNull
  private final  String tenantId;

  @NotNull
  private final  BookKeepingDimension dimension;

  @NotNull
  private final  EventStatus status;

  @NotNull
  private final  String useCase;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private final  LocalDate date1;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private final  LocalDate date2;

  private final  Long gsn;

  private final  List<BookingItemMessage> bookingItems = new ArrayList<>();

  /*
  public void addEventItem(BookingItemMessage bookingItem) {
    this.bookingItems.add(bookingItem);
  }*/


}
