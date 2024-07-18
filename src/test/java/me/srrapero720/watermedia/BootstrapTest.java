package me.srrapero720.watermedia;

import me.srrapero720.watermedia.loader.ILoader;

public class BootstrapTest {
    public static void main(String... args) {
        try {
            WaterMedia.prepare(ILoader.DEFAULT).start();
        } catch (Exception e) {
            WaterMedia.LOGGER.fatal("FAILED TO LOAD WATERMEDIA", e);
        }
    }
}
