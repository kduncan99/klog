package com.bearsnake.klog;

import java.util.Arrays;

public abstract class Writer {

    protected Level _level = null;

    public void close() {}
    public void open() {}
    public final Writer setLevel(final Level level) { _level = level; return this; }

    public final synchronized void write(Level level, String category, String message, Object... parameters) {
        if ((_level == null) || (level.ordinal() <= _level.ordinal())) {
            var pfx = category == null ? "" : "[" + category + "] ";
            var text = pfx + String.format(message, parameters);
            _write(text);
        }
    }

    public final synchronized void write(Level level, String category, String[] messages) {
        if ((_level == null) || (level.ordinal() <= _level.ordinal())) {
            var pfx = category == null ? "" : "[" + category + "] ";
            Arrays.stream(messages).map(msg -> pfx + msg).forEach(this::_write);
        }
    }

    protected abstract void _write(final String message);
}
