package ch.cloudcoins;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static ch.cloudcoins.DateFormatUtil.INTERNATIONAL_DATE_FORMATTER;
import static java.time.temporal.ChronoUnit.SECONDS;

public class DateUtil {

    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return instantToLocalDateTime(Instant.ofEpochMilli(timestamp));
    }

    public static LocalDate instantToLocalDate(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate parseInternationalFormat(String input) {
        return LocalDate.parse(input, INTERNATIONAL_DATE_FORMATTER);
    }

    public static LocalDate parseWithPattern(String input, String pattern) {
        return LocalDate.parse(input, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate dateInFuture(int days) {
        return LocalDate.now().plusDays(days);
    }

    public static boolean dateBetweenInclusive(LocalDate test, LocalDate start, LocalDate end) {
        return test.equals(start) || test.equals(end) || (start.isBefore(test) && end.isAfter(test));
    }

    public static int compareDateTimeWithSecondsPrecision(LocalDateTime a, LocalDateTime b) {
        return a.truncatedTo(SECONDS).compareTo(b.truncatedTo(SECONDS));
    }

    public static boolean equalDateTimeWithSecondsPrecision(LocalDateTime a, LocalDateTime b) {
        return a.truncatedTo(SECONDS).equals(b.truncatedTo(SECONDS));
    }
}
