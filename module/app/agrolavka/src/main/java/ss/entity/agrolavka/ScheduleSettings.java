package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ss.entity.security.EntityAudit;

@Entity
@Table(name = "show_products_schedule_settings")
public class ScheduleSettings extends EntityAudit {

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "from_hours", nullable = false)
    private Integer fromHours;

    @Column(name = "from_minutes", nullable = false)
    private Integer fromMinutes;

    @Column(name = "to_hours", nullable = false)
    private Integer toHours;

    @Column(name = "to_minutes", nullable = false)
    private Integer toMinutes;

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getFromHours() {
        return fromHours;
    }

    public void setFromHours(Integer fromHours) {
        this.fromHours = fromHours;
    }

    public Integer getFromMinutes() {
        return fromMinutes;
    }

    public void setFromMinutes(Integer fromMinutes) {
        this.fromMinutes = fromMinutes;
    }

    public Integer getToHours() {
        return toHours;
    }

    public void setToHours(Integer toHours) {
        this.toHours = toHours;
    }

    public Integer getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(Integer toMinutes) {
        this.toMinutes = toMinutes;
    }
}
