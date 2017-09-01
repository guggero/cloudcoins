package ch.cloudcoins;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DateFormatUtil {

    public static final String SWISS_DATE_FORMAT = "dd.MM.yyyy";
    public static final String SWISS_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    public static final String SWISS_DATE_LONG_FORMAT = "dd. MMMM yyyy";
    public static final String CSV_DATE_FORMAT = "dd.MM.yyyy '00:00'[':00']";
    public static final String INTERNATIONAL_DATE_FORMAT = "yyyy-MM-dd";

    public static final DateTimeFormatter SWISS_DATE_FORMATTER = ofPattern(SWISS_DATE_FORMAT);
    public static final DateTimeFormatter SWISS_DATE_TIME_FORMATTER = ofPattern(SWISS_DATE_TIME_FORMAT);
    public static final DateTimeFormatter SWISS_DATE_LONG_FORMATTER = ofPattern(SWISS_DATE_LONG_FORMAT);
    public static final DateTimeFormatter INTERNATIONAL_DATE_FORMATTER = ofPattern(INTERNATIONAL_DATE_FORMAT);

    public static String formatDateSwissFormat(LocalDate date) {
        return SWISS_DATE_FORMATTER.format(date);
    }

    public static String formatDateTimeSwissFormat(LocalDateTime date) {
        return SWISS_DATE_TIME_FORMATTER.format(date);
    }

    public static String formatDateSwissLongFormat(LocalDate date, Locale locale) {
        return SWISS_DATE_LONG_FORMATTER.withLocale(locale).format(date);
    }

    public static String formatDateInternationalFormat(LocalDate date) {
        return INTERNATIONAL_DATE_FORMATTER.format(date);
    }

    public static String formatSwissDateInFuture(int days) {
        return SWISS_DATE_FORMATTER.format(DateUtil.dateInFuture(days));
    }
}
