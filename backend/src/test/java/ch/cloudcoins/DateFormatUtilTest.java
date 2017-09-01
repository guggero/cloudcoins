package ch.cloudcoins;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateFormatUtilTest {

    @Test
    public void shouldReturnSwissDateFormat() {
        //given
        LocalDate localDate = LocalDate.of(2016, 2, 20);

        //when
        String result = DateFormatUtil.formatDateSwissFormat(localDate);

        //then
        assertThat(result, is("20.02.2016"));

    }

    @Test
    public void shouldReturnSwissDateTimeFormat() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2014, 9, 9, 12, 30);

        //when
        String result = DateFormatUtil.formatDateTimeSwissFormat(localDateTime);

        //then
        assertThat(result, is("09.09.2014 12:30"));
    }

    @Test
    public void shouldReturnSwissDateLongFormat() {
        //given
        LocalDate localDate = LocalDate.of(2016, 2, 20);

        //when
        String result = DateFormatUtil.formatDateSwissLongFormat(localDate, Locale.GERMAN);

        //then
        assertThat(result, is("20. Februar 2016"));

    }

    @Test
    public void shouldReturnInternationalDateFormat() {
        //given
        LocalDate localDate = LocalDate.of(2016, 2, 20);

        //when
        String result = DateFormatUtil.formatDateInternationalFormat(localDate);

        //then
        assertThat(result, is("2016-02-20"));

    }

    @Test
    public void shouldReturnSwissDateInFutureFormat() {
        //given
        LocalDate localDate = LocalDate.of(2016, 2, 20);

        //when
        String result = DateFormatUtil.formatSwissDateInFuture(1);

        //then
        assertThat(result, is(DateFormatUtil.formatDateSwissFormat(LocalDate.now().plusDays(1))));

    }

}