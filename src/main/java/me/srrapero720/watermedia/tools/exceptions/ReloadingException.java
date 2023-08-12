package me.srrapero720.watermedia.tools.exceptions;

public class ReloadingException extends Exception {
    public ReloadingException(String module) {
        super("Rejected attempt to reload " + module, null);
    }
}
