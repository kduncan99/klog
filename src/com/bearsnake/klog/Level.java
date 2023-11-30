package com.bearsnake.klog;

public enum Level {
    NONE(""),
    FATAL("FATAL"),
    ERROR("ERROR"),
    WARNING("WARN "),
    INFO("INFO "),
    DEBUG("DEBUG"),
    TRACE("TRACE"),
    ALL("");

    private final String _token;

    Level(final String token) { _token = token; }

    @Override
    public String toString() { return _token; }
}
