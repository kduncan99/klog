package com.bearsnake.klog;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

    private Level _level = null;
    private final ConcurrentLinkedQueue<String> _categories = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Writer> _writers = new ConcurrentLinkedQueue<>();

    public Logger addCategory(final String category) { _categories.add(category); return this; }
    public Logger addWriter(final Writer writer) { _writers.add(writer); return this; }
    public Logger close() { _writers.forEach(Writer::close); return this; }
    public Logger open() { _writers.forEach(Writer::open); return this; }
    public Logger removeCategory(final String category) { _categories.remove(category); return this; }
    public Logger setLevel(final Level level) { _level = level; return this; }

    public Logger debug(final String message) {
        return write(Level.DEBUG, null, message);
    }

    public Logger debug(final String category, final String message) {
        return write(Level.DEBUG, category, message);
    }

    public Logger debugf(final String message, Object... parameters) {
        return write(Level.DEBUG, null, message, parameters);
    }

    public Logger debugf(final String category, final String message, Object... parameters) {
        return write(Level.DEBUG, category, message, parameters);
    }

    public Logger error(final String message) {
        return write(Level.ERROR, null, message);
    }

    public Logger error(final String category, final String message) {
        return write(Level.ERROR, category, message);
    }

    public Logger errorf(final String message, Object... parameters) {
        return write(Level.ERROR, null, message, parameters);
    }

    public Logger errorf(final String category, final String message, Object... parameters) {
        return write(Level.ERROR, category, message, parameters);
    }

    public void fatal(final String message) {
        write(Level.FATAL, null, message);
        throw new RuntimeException(message);
    }

    public void fatal(final String category, final String message) {
        write(Level.FATAL, category, message);
        throw new RuntimeException(message);
    }

    public void fatalf(final String message, Object... parameters) {
        write(Level.FATAL, null, message, parameters);
        throw new RuntimeException(message);
    }

    public void fatalf(final String category, final String message, Object... parameters) {
        write(Level.FATAL, category, message, parameters);
        throw new RuntimeException(message);
    }

    public Logger info(final String message) {
        return write(Level.INFO, null, message);
    }

    public Logger info(final String category, final String message) {
        return write(Level.INFO, category, message);
    }

    public Logger infof(final String message, Object... parameters) {
        return write(Level.INFO, null, message, parameters);
    }

    public Logger infof(final String category, final String message, Object... parameters) {
        return write(Level.INFO, category, message, parameters);
    }

    public Logger trace(final String message) {
        return write(Level.TRACE, null, message);
    }

    public Logger trace(final String category, final String message) {
        return write(Level.TRACE, category, message);
    }

    public Logger tracef(final String message, Object... parameters) {
        return write(Level.TRACE, null, message, parameters);
    }

    public Logger tracef(final String category, final String message, Object... parameters) {
        return write(Level.TRACE, category, message, parameters);
    }

    public Logger warning(final String message) {
        return write(Level.WARNING, null, message);
    }

    public Logger warning(final String category, final String message) {
        return write(Level.WARNING, category, message);
    }

    public Logger warningf(final String message, Object... parameters) {
        return write(Level.WARNING, null, message, parameters);
    }

    public Logger warningf(final String category, final String message, Object... parameters) {
        return write(Level.WARNING, category, message, parameters);
    }

    public Logger catching(final Throwable t) {
        write(Level.ERROR, null, String.format("Catching %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        write(Level.ERROR, null, arr);
        return this;
    }

    public Logger catching(final String category, final Throwable t) {
        write(Level.ERROR, category, String.format("Catching %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        write(Level.ERROR, category, arr);
        return this;
    }

    public Logger throwing(final Throwable t) {
        write(Level.ERROR, null, String.format("Throwing %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        write(Level.ERROR, null, arr);
        return this;
    }

    public Logger throwing(final String category, final Throwable t) {
        write(Level.ERROR, category, String.format("Throwing %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        write(Level.ERROR, category, arr);
        return this;
    }

    public Logger write(
        final Level level,
        final String category,
        final String message,
        final Object... parameters
    ) {
        if ((_level == null) || (level.ordinal() <= _level.ordinal())) {
            if (_categories.isEmpty() || _categories.contains(category)) {
                _writers.forEach(w -> w.write(level, category, message, parameters));
            }
        }
        return this;
    }

    public Logger write(
        final Level level,
        final String category,
        final String[] messages
    ) {
        _writers.forEach(w -> w.write(level, category, messages));
        return this;
    }
}
