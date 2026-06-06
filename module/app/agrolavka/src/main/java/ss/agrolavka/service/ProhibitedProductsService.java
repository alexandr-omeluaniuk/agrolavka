package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ProhibitedProductsService {

    @Autowired
    private SystemSettingsService systemSettingsService;

    public Boolean isTimeForShowProhibited() {
        final var settings = systemSettingsService.getScheduleSettings();
        final var now = LocalDateTime.now().atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Europe/Minsk"));
        final var nowDayOfWeek = now.getDayOfWeek();
        final var nowHours = now.getHour();
        final var nowMinutes = now.getMinute();
        final var nowTimestamp = nowHours * 60 + nowMinutes;
        final var matched = settings.stream().filter(s -> {
            final var fromTimestamp = s.getFromHours() * 60 + s.getFromMinutes();
            final var toTimestamp = s.getToHours() * 60 + s.getToMinutes();
            return DayOfWeek.of(s.getDayOfWeek()) == nowDayOfWeek &&
                fromTimestamp < nowTimestamp && nowTimestamp < toTimestamp;
        }).toList();
        return !matched.isEmpty();
    }

    public Boolean isSpecialProductsMustBeHidden() {
        final var showAll = systemSettingsService.getCurrentSettings().isShowAllProductVariants();
        final var isTimeForProhibited = isTimeForShowProhibited();
        return !showAll && isTimeForProhibited;
    }
}
