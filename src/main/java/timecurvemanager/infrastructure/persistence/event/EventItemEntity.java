package timecurvemanager.infrastructure.persistence.event;

import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "event_item"
        , indexes = {
//        @Index(name = "idx_item_event_entity", columnList = "eventEntity", unique = false),
        @Index(name = "idx_item_timecurve_date1", columnList = "timecurve_id, date1", unique = false),
        @Index(name = "idx_item_timecurve_date2", columnList = "timecurve_id, date2", unique = false)
}
)
public class EventItemEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private EventEntity eventEntity;

    @Column(name = "row_nr")
    private Integer rowNr;

    @Column(name = "tenant_id")
    private String tenantId;

    private EventDimension dimension;

    @Column(name = "timecurve_id")
    private Long timecurveId;

    @Column(name = "item_type")
    private EventItemType itemType;

    @Column(name = "item_id")
    private Long itemId;

    private LocalDate date1;

    private LocalDate date2;

    private BigDecimal value1;

    private BigDecimal value2;

    private BigDecimal value3;

    private BigDecimal tover1;

    private BigDecimal tover2;

    private BigDecimal tover3;

}
