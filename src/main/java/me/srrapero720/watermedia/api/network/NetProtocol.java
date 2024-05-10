package me.srrapero720.watermedia.api.network;

public abstract class NetProtocol {
    protected final String protocol;
    public NetProtocol(String protocol) {
        this.protocol = protocol;
    }

    protected boolean match(String url) {
        return url.startsWith(this.protocol + "://");
    }
    protected abstract String parse(String url);
}
