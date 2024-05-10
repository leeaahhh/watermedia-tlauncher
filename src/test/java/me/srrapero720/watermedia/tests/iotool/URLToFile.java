package me.srrapero720.watermedia.tests.iotool;

import me.srrapero720.watermedia.tests.Test;

import java.io.File;
import java.net.URL;

public class URLToFile {
    public static void main(String... args) throws Exception {
        URL url = new URL("file:///C:\\duck.mp4");
        URL url2 = new URL("C:\\duck.mp4");
        File file = new File(url.toString().substring("file:/".length()));

        Test.LOGGER.info(url.toString());
        Test.LOGGER.info(file.toString());
        Test.LOGGER.info(url.toURI().toString());
        Test.LOGGER.info(url.toURI().toURL().toString());
        Test.LOGGER.info(url.toURI().toASCIIString());
        Test.LOGGER.info(file.getAbsoluteFile().toString());

        Test.LOGGER.info("File relative {}", file.exists() ? "exists" : "doesn't exists");
        Test.LOGGER.info("File absolute {}", file.getAbsoluteFile().exists() ? "exists" : "doesn't exists");
    }
}
