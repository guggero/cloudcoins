package ch.cloudcoins;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DummyTest {

    @Test
    public void shouldGenerateTestResult() {
        assertThat("true", is("true"));
    }
}
