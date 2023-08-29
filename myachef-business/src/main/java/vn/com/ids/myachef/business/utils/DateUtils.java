package vn.com.ids.myachef.business.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDate parseLocalDate(String dateStr, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(dateStr, formatter);
        } catch (Exception ex) {
            log.error(String.format("ERROR Message: %s, date: '%s', datePattern: '%s'", ex.getMessage(), dateStr, datePattern));
        }
        return localDate;
    }

    public static LocalDateTime getEndDateOfMonthlyPeriod(LocalDateTime dateTime) {
        return dateTime.plusMonths(1);
    }

    public static LocalDateTime getEndDateOfYearlyPeriod(LocalDateTime dateTime) {
        return dateTime.plusYears(1);
    }

    public static LocalDateTime getEndDateOfTrialPeriod(LocalDateTime dateTime, int numOfDate) {
        return dateTime.plusDays(numOfDate);
    }
}
