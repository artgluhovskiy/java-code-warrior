package org.art.web.warrior.client.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileReaderUtil {

    private FileReaderUtil() {
    }

    public static String readFileIntoString(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = FileReaderUtil.class.getResourceAsStream(filePath)) {
            LineIterator lineIterator = IOUtils.lineIterator(in, StandardCharsets.UTF_8);
            while (lineIterator.hasNext()) {
                sb.append(lineIterator.nextLine());
            }
        } catch (IOException e) {
            log.warn("Cannot read source code file! Path: {}", filePath);
        }
        return sb.toString();
    }
}
