package me.srrapero720.watermedia.api.network;

import java.io.Closeable;
import java.io.IOException;

public class NetFetch implements Closeable {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0";
    public static final String REAL_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";

    public NetFetch() {

    }

    public NetFetch(NetSource source) {

    }

    @Override
    public void close() throws IOException {

    }

    public enum Method {
        GET, POST, PUT, DELETE;

        @Override
        public String toString() {
            return this.name().toUpperCase();
        }
    }
}
