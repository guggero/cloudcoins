package ch.cloudcoins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtil {

    public static String readUtf8(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, UTF_8);
    }

    public static String readUtf8(String fileName) throws IOException {
        return readUtf8(new File(fileName));
    }
}
