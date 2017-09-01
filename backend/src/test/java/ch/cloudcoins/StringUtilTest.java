package ch.cloudcoins;

import org.hamcrest.core.Is;
import org.junit.Test;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StringUtilTest {

    private static final String EMPTY_STRING = "";

    @Test
    public void shouldReturnNotContainsOnlyDigitsIfInputNull() {
        //given
        String strToValidate = null;

        //when
        boolean containsOnlyDigits = StringUtil.containsOnlyDigits(strToValidate);

        //then
        assertThat(containsOnlyDigits, is(FALSE));

    }

    @Test
    public void shouldReturnNotContainsOnlyDigitsIfInputEmpty() {
        //given
        String strToValidate = EMPTY_STRING;

        //when
        boolean containsOnlyDigits = StringUtil.containsOnlyDigits(strToValidate);

        //then
        assertThat(containsOnlyDigits, is(FALSE));
    }

    @Test
    public void shouldReturnNotContainsOnlyDigits() {
        //given
        String strToValidate = "124521ds568";

        //when
        boolean containsOnlyDigits = StringUtil.containsOnlyDigits(strToValidate);

        //then
        assertThat(containsOnlyDigits, is(FALSE));
    }

    @Test
    public void shouldReturnContainsOnlyDigits() {
        //given
        String strToValidate = "124500568";

        //when
        boolean containsOnlyDigits = StringUtil.containsOnlyDigits(strToValidate);

        //then
        assertThat(containsOnlyDigits, is(TRUE));
    }

    @Test
    public void shouldReturnNullWithNullString() {
        // when
        String result = StringUtil.trimNullSafe(null);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldReturnTrimmedStringWithNotNullString() {
        // given
        String notTrimmedString = "   string   ";
        String trimmedString = "string";
        // when
        String result = StringUtil.trimNullSafe(notTrimmedString);

        // then
        assertThat(result, is(Is.is(notNullValue())));
        assertThat(result.length(), is(not(notTrimmedString.length())));
        assertThat(result.length(), is(trimmedString.length()));
        assertThat(result, is(trimmedString));
    }

    @Test
    public void shouldReturnEmptyTrimmedStringWithSpacedString() {
        // when
        String result = StringUtil.trimNullSafe("     ");

        // then
        assertThat(result, is(Is.is(notNullValue())));
        assertThat(result, is(EMPTY_STRING));
    }

}
