// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class provides methods for client software to generate log entries in some one or more log destinations.
 * The logger itself filters on two log entry attributes:
 *  1) All log entries are associated with a log level. The object contains a LevelMask which indicates which levels
 *  are to be allowed - if a given log entry does not match the LevelMask it is filtered out.
 *  TODO - filters on class names and maybe methods
 * Any log entries which pass the filters listed above are routed to the registered Writer objects, which may
 * apply additional filtering, and which will format and persist any non-filtered log entries to the appropriate
 * destinations.
 */
public class Logger {

    private LevelMask _levelMask;
    private final String _name;
    private final ConcurrentLinkedQueue<Writer> _writers = new ConcurrentLinkedQueue<>();

    /**
     * Creates a new Logger with the given name, no writers, and the default level mask value.
     */
    public Logger(
        final String name
    ) {
        _name = name;
        _levelMask = new LevelMask(Level.ERROR);
    }

    /**
     * Creates a new Logger with the given name, no writers, and a specific level mask value.
     */
    public Logger(
        final String name,
        final LevelMask levelMask
    ) {
        _name = name;
        _levelMask = levelMask;
    }

    /**
     * Creates a new Logger with the given name, no writers, and a level mask value which represents
     * all Levels at or above the specified Level priority.
     */
    public Logger(
        final String name,
        final Level level
    ) {
        _name = name;
        _levelMask = new LevelMask(level);
    }

    /**
     * Creates a new logger with the given name, which inherits the current settings of the provided logger
     * (apart from the logger name)
     */
    public Logger(
        final String name,
        final Logger source
    ) {
        _name = name;
        _levelMask = source._levelMask;
        _writers.addAll(source._writers);
    }

    /**
     * Registers a Writer with this logger. Any given writer may be registered with multiple Logger objects.
     * This will automatically invoke the open() method of the given Writer.
     * @param writer the Writer object to be registered.
     * @return this object
     */
    public Logger addWriter(final Writer writer) { _writers.add(writer); return this; }

    /**
     * Causes close() to be invoked upon all Writer objects which are registered with this Logger.
     * Well-written clients should endeavor to ensure to invoke this method on every extant Logger object.
     * @return this object
     */
    public Logger close() {
        for (Writer w : _writers) {
            try {
                w.close(this);
            } catch (IOException ex) {
                // nothing to do
            }
        }
        return this;
    }

    /**
     * Retrieves the current LevelMask, which may then be modified using its various methods
     * to effect a change in the logging profile for this Logger.
     * @return reference to this Logger's LevelMask object
     */
    public LevelMask getLevelMask() {
        return _levelMask;
    }

    /**
     * Retrieves the name of the logger
     */
    public String getName() {
        return _name;
    }

    /**
     * Replaces the current level mask with a new value representing all levels at or above
     * the given level in priority.
     * @param level the level to be used for establishing the new level mask
     * @return this object
     */
    public Logger setLevel(
        final Level level
    ) {
        _levelMask = new LevelMask(level);
        return this;
    }

    /**
     * Replaces the current level mask with the given mask
     * @param levelMask the new mask to be used
     * @return this object
     */
    public Logger setLevelMask(
        final LevelMask levelMask
    ) {
        _levelMask = levelMask;
        return this;
    }

    /**
     * Creates a debug log entry with a message formatter and parameters for the message
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger debug(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.DEBUG, formatter, parameters);
    }

    /**
     * Creates an error log entry with a message formatter and parameters for the message
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger error(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.ERROR, formatter, parameters);
    }

    /**
     * Creates a fatal log entry with a message formatter and parameters for the message
     * It should be noted that after a fatal log entry is processed, a RuntimeException is thrown.
     * Thus, this method never really returns a value.
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger fatal(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.FATAL, formatter, parameters);
    }

    /**
     * Creates an info log entry with a message formatter and parameters for the message
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger info(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.INFO, formatter, parameters);
    }

    /**
     * Creates a trace log entry with a message formatter and parameters for the message
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger trace(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.TRACE, formatter, parameters);
    }

    /**
     * Creates a warning log entry with a message formatter and parameters for the message
     * @param formatter Message formatter - if there are no parameters, this is just a message.
     * @param parameters parameters to be placed into the given message formatter
     * @return this object
     */
    public Logger warning(
        final String formatter,
        Object... parameters
    ) {
        return write(Level.WARNING, formatter, parameters);
    }

    /**
     * Convenience method to create a set of error log entries describing a caught exception
     * @param t caught exception
     * @return this object
     */
    public Logger catching(
        final Throwable t
    ) {
        write(Level.ERROR, String.format("Catching %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        return writeMultiple(Level.ERROR, arr);
    }

    /**
     * Convenience method to create a set of error log entries describing an exception which the client
     * is preparing to throw.
     * @param t exception to be thrown
     * @return this object
     */
    public Logger throwing(
        final Throwable t
    ) {
        write(Level.ERROR, String.format("Throwing %s", t));
        var arr = Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
        return writeMultiple(Level.ERROR, arr);
    }

    /**
     * Writes a log entry as directed by the given parameters.
     * If no parameters are needed, the message formatter is just a message string, and the parameters list is empty.
     * @param level indicates the level for the log entry
     * @param formatter a message formatter indicating the format of the message to be logged
     * @param parameters parameters which are used to fill in placeholders in the message formatter
     * @return this object
     */
    public Logger write(
        final Level level,
        final String formatter,
        final Object... parameters
    ) {
        if (_levelMask.matches(level)) {
            _writers.forEach(w -> w.write(this, level, formatter, parameters));
        }

        if (level == Level.FATAL) {
            throw new RuntimeException(String.format(formatter, parameters));
        }

        return this;
    }

    /**
     * Writes a series of log entries as directed by the given parameters
     * @param level indicates the level for the log entries
     * @param messages an array of strings - one log entry is posted for each message
     * @return this object
     */
    public Logger writeMultiple(
        final Level level,
        final String[] messages
    ) {
        if (_levelMask.matches(level)) {
            _writers.forEach(w -> w.writeMultiple(this, level, messages));
        }

        if (level == Level.FATAL) {
            if (messages.length > 0) {
                throw new RuntimeException(messages[0]);
            } else {
                throw new RuntimeException("Fatal Log Entry created with no messages");
            }
        }

        return this;
    }

    /**
     * Creates a multi-line log entry displaying the given buffer as a table of hex values, byte-by-byte.
     * @param level logging level
     * @param buffer buffer
     * @return this object
     */
    public Logger writeBuffer(
        final Level level,
        final byte[] buffer
    ) {
        return writeBuffer(level, null, buffer);
    }

    /**
     * Creates a multi-line log entry displaying the given buffer as a table of hex values, byte-by-byte.
     * @param level logging level
     * @param caption caption for the table - if null, no caption is emitted
     * @param buffer buffer
     * @return this object
     */
    public Logger writeBuffer(
        final Level level,
        final String caption,
        final byte[] buffer
    ) {
        var lines = (buffer.length / 16) + ((buffer.length % 16) > 0 ? 1 : 0);
        String[] strings;
        int sx = 0;
        if (caption != null) {
            strings = new String[lines + 1];
            strings[0] = caption;
            sx = 1;
        } else {
            strings = new String[lines];
        }

        for (int bx = 0; bx < buffer.length; bx += 16) {
            var sb = new StringBuilder();
            sb.append(String.format("%06X", bx)).append(":");
            for (int by = 0; by < 16; by++) {
                int bz = bx + by;
                if (bz >= buffer.length) {
                    break;
                }
                sb.append(" ").append(String.format("%02X", buffer[bz]));
            }
            strings[sx++] = sb.toString();
        }

        return writeMultiple(level, strings);
    }
}
