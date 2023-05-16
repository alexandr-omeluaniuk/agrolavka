package ss.entity.martin;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import ss.entity.security.EntityAudit;
import ss.martin.core.anno.Updatable;

/**
 * Calendar event.
 * @author ss
 */
@MappedSuperclass
public abstract class CalendarEvent extends EntityAudit {
// ============================================ FIELDS ================================================================
    /** Event start datetime. */
    @Updatable
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "event_start", nullable = false)
    private Date start;
    /** Event start datetime. */
    @Updatable
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_end", nullable = false)
    private Date end;
    /** Event title. */
    @Transient
    private String eventTitle;
// ============================================ SET & GET =============================================================
    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }
    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }
    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }
    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    /**
     * @return the eventTitle
     */
    public String getEventTitle() {
        return eventTitle;
    }
    /**
     * @param eventTitle the eventTitle to set
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
