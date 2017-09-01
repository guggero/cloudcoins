package ch.cloudcoins;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class FileUtilTest {

    @Test
    public void shouldReadFileUTF8() throws IOException {
        // when
        String result = FileUtil.readUtf8("src/test/resources/META-INF/persistence.xml");

        // then
        assertThat(result, containsString("cloudcoins-test"));
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOException() throws IOException {
        // when
        String result = FileUtil.readUtf8("/not/existing.file");

        // then expect exception
    }
}