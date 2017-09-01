package ch.cloudcoins;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class DateUtilTest {

    @Test
    public void shouldReturnNullForNullInput() {
        // when
        Date result = DateUtil.localDateToDate(null);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldConvertLocalDateToDate() {
        // given
        LocalDate localDate = LocalDate.of(2016, 2, 20);

        // when
        Date result = DateUtil.localDateToDate(localDate);

        // then
        assertThat(result.toString(), is("Sat Feb 20 00:00:00 CET 2016"));
    }

    @Test
    public void shouldReturnTrueWhenTimesHasTheSameSecondsPrecision() {
        //given
        LocalDateTime dateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 45);

        //when
        boolean result = DateUtil.equalDateTimeWithSecondsPrecision(dateTime, dateTime);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenTimesHasNotTheSameSecondsPrecision() {
        //given
        LocalDateTime firstDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 45);
        LocalDateTime secondDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 44);

        //when
        boolean result = DateUtil.equalDateTimeWithSecondsPrecision(firstDateTime, secondDateTime);

        //then
        assertThat(result, is(false));
    }


    @Test
    public void shouldReturnZeroWhenTimesHasTheSameSecondsPrecision() {
        //given
        LocalDateTime dateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 45);

        //when
        int result = DateUtil.compareDateTimeWithSecondsPrecision(dateTime, dateTime);

        //then
        assertThat(result, is(0));
    }

    @Test
    public void shouldReturnOneWhenFirstTimesIsBiggerThenSecondTimeWithSecondsPrecision() {
        //given
        LocalDateTime firstDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 45);
        LocalDateTime secondDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 44);

        //when
        int result = DateUtil.compareDateTimeWithSecondsPrecision(firstDateTime, secondDateTime);

        //then
        assertThat(result, is(1));
    }

    @Test
    public void shouldReturnMinusOneWhenFirstTimesIsLesserThenSecondTimeWithSecondsPrecision() {
        //given
        LocalDateTime firstDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 44);
        LocalDateTime secondDateTime = LocalDateTime.of(2014, 9, 9, 19, 46, 45);

        //when
        int result = DateUtil.compareDateTimeWithSecondsPrecision(firstDateTime, secondDateTime);

        //then
        assertThat(result, is(-1));
    }

    @Test
    public void shouldReturnTrueWhenDatesAreEquals() {
        //given
        LocalDate test = LocalDate.of(2016, 2, 20);
        LocalDate start = LocalDate.of(2016, 2, 20);
        LocalDate end = LocalDate.of(2016, 2, 20);

        //when
        boolean result = DateUtil.dateBetweenInclusive(test, start, end);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnTrueWhenDateIsBetweenTwoDates() {
        //given
        LocalDate test = LocalDate.of(2016, 2, 20);
        LocalDate start = LocalDate.of(2016, 2, 19);
        LocalDate end = LocalDate.of(2016, 2, 21);

        //when
        boolean result = DateUtil.dateBetweenInclusive(test, start, end);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenDateIsNotBetweenTwoDates() {
        //given
        LocalDate test = LocalDate.of(2016, 2, 20);
        LocalDate start = LocalDate.of(2016, 2, 22);
        LocalDate end = LocalDate.of(2016, 2, 21);

        //when
        boolean result = DateUtil.dateBetweenInclusive(test, start, end);

        //then
        assertThat(result, is(false));
    }

}
