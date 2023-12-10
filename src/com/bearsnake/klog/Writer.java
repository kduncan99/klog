// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Writer {

    protected static class PrefixSpec {

        protected final PrefixEntity _entity;
        protected final PrefixEntityWidthSpecifier _widthSpecifier;
        protected final Integer _width;

        protected PrefixSpec(
            final PrefixEntity entity,
            final PrefixEntityWidthSpecifier widthSpecifier,
            final int width
        ) {
            _entity = entity;
            _widthSpecifier = widthSpecifier;
            _width = width;
        }
    }

    private char[] _prefixDelimiters = new char[]{' '};
    protected LevelMask _levelMask;
    protected final List<PrefixSpec> _prefixSpecifiers = new LinkedList<>();
    protected String _spaces = "";

    /**
     * Creates a new Writer with a specific level mask value.
     */
    protected Writer(
        final LevelMask levelMask
    ) {
        _levelMask = levelMask;
    }

    /**
     * Adds a prefix entry to this writer.
     * A prefix entry contains information such as time/date, category, error level, etc.
     * @param entity The entity to be included in the prefix
     * @return this object
     */
    public Writer addPrefixEntity(
        final PrefixEntity entity
    ) {
        _prefixSpecifiers.add(new PrefixSpec(entity, PrefixEntityWidthSpecifier.NONE, 0));
        return this;
    }

    /**
     * Adds a prefix entry to this writer.
     * A prefix entry contains information such as time/date, category, error level, etc.
     * @param entity The entity to be included in the prefix
     * @param widthSpec Specifies if and how the width of the output for this entity is to be limited
     * @param width The width limit for the output of this entity
     * @return this object
     */
    public Writer addPrefixEntity(
        final PrefixEntity entity,
        final PrefixEntityWidthSpecifier widthSpec,
        final Integer width
    ) {
        _prefixSpecifiers.add(new PrefixSpec(entity, widthSpec, width));
        if (width > _spaces.length()) {
            _spaces = new String(new char[10]).replace('\0', ' ');
        }
        return this;
    }

    /**
     * Clears any current prefix entities from the current writer.
     * Useful for clearing out any default settings for a particular writer before specifying custom output.
     * @return this object
     */
    public Writer clearPrefixEntities() {
        _prefixSpecifiers.clear();
        return this;
    }

    /**
     * Sets the delimiter character to be used between prefix entities in the log entry output.
     * Typical values include a space or a colon, but can be any character
     * @param ch delimiter to be used
     * @return this object
     */
    public Writer setPrefixDelimiter(
        final char ch
    ) {
        _prefixDelimiters = new char[] { ch };
        return this;
    }

    /**
     * Sets the delimiter characters to be used surrounding, or between prefix entities in the log entry output.
     * If one character is specified, it is placed between the various prefix entities in the log entry output.
     * Typical values are ' ' or ':'.
     * If two or more characters are specified, the first character is placed in front of each entity, and the
     * second character is placed at the end of each entity. Any additional characters are ignored.
     * Typical values are '[' and ']', or '{' and '}'.
     * @param delimiters delimiter character(s)
     * @return this object
     */
    public Writer setPrefixDelimiters(
        final char[] delimiters
    ) {
        _prefixDelimiters = delimiters;
        return this;
    }

    /**
     * Sets the delimiter characters to be used surrounding, or between prefix entities in the log entry output.
     * If one character is specified, it is placed between the various prefix entities in the log entry output.
     * Typical values are " " or ":".
     * If two or more characters are specified, the first character is placed in front of each entity, and the
     * second character is placed at the end of each entity. Any additional characters are ignored.
     * Typical values are "[]" or "{}".
     * @param delimiters delimiter character(s)
     * @return this object
     */
    public Writer setPrefixDelimiters(
        final String delimiters
    ) {
        _prefixDelimiters = delimiters.toCharArray();
        return this;
    }

    /**
     * Closes this writer, ensuring all buffered data is written to the destination.
     * The default action is to do nothing.
     * @param logger the logger which is invoking the method.
     */
    public void close(final Logger logger) throws IOException {}

    /**
     * Presents a log entry to the writer.
     * @param logger the logger object which is invoking this
     * @param level level of the log entry
     * @param formatter message formatter
     * @param parameters parameters to be inserted into the message formatter
     */
    public final synchronized void write(
        final Logger logger,
        final Level level,
        final String formatter,
        final Object... parameters
    ) {
        if (_levelMask.matches(level)) {
            var pfx = createPrefix(logger, level);
            var text = pfx + " " + String.format(formatter, parameters);
            _write(text);
        }
    }

    /**
     * Presents a set of log entries to the writer, to be written in one operation.
     * @param logger the logger object which is invoking this
     * @param level level of the log entry
     * @param messages messages to be written
     */
    public final synchronized void writeMultiple(
        final Logger logger,
        final Level level,
        final String[] messages
    ) {
        if (_levelMask.matches(level)) {
            var pfx = createPrefix(logger, level);
            Arrays.stream(messages).map(msg -> pfx + " " + msg).forEach(this::_write);
        }
    }

    private static StackTraceElement findCaller() {
        var st = Thread.currentThread().getStackTrace();
        var sx = st.length - 1;
        var ste = st[sx];
        for (; sx >= 0; sx--) {
            if (st[sx].getClassName().startsWith("com.bearsnake.klog")) {
                break;
            }
            ste = st[sx];
        }
        return ste;
    }

    /**
     * Creates the prefix for the log entry, based on the prefix entities
     */
    private String createPrefix(
        final Logger logger,
        final Level level
    ) {
        if (_prefixSpecifiers.isEmpty()) {
            return "";
        } else {
            var ste = findCaller();
            var fileName = ste.getFileName();
            var lineNumber = String.valueOf(ste.getLineNumber());
            var clSplit = ste.getClassName().split("\\.");
            var packageName = String.join(".", Arrays.copyOf(clSplit, clSplit.length - 1));
            var className = clSplit[clSplit.length - 1];
            var methodName = ste.getMethodName();

            StringBuilder sb = new StringBuilder();
            for (var pfxSpec : _prefixSpecifiers) {
                String text = switch (pfxSpec._entity) {
                    case LEVEL -> level.toString();
                    case LOGGER_NAME -> logger.getName();
                    case DATE_AND_TIME -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
                    case SOURCE_PACKAGE -> packageName;
                    case SOURCE_CLASS -> className;
                    case SOURCE_METHOD -> methodName;
                    case SOURCE_FILE_NAME -> fileName;
                    case SOURCE_LINE_NUMBER -> lineNumber;
                };

                if (text == null) {
                    text = "";
                }

                if (pfxSpec._widthSpecifier == PrefixEntityWidthSpecifier.FIXED) {
                    text = (text + _spaces).substring(0, pfxSpec._width);
                } else if (pfxSpec._widthSpecifier == PrefixEntityWidthSpecifier.MAXIMUM) {
                    if (text.length() > pfxSpec._width) {
                        text = text.substring(0, pfxSpec._width);
                    }
                } else if (pfxSpec._widthSpecifier == PrefixEntityWidthSpecifier.MINIMUM) {
                    if (text.length() < pfxSpec._width) {
                        text = (text + _spaces).substring(0, pfxSpec._width);
                    }
                }

                if (!sb.isEmpty() && (_prefixDelimiters.length == 1)) {
                    sb.append(_prefixDelimiters[0]);
                } else if (_prefixDelimiters.length > 1) {
                    sb.append(_prefixDelimiters[0]);
                }

                sb.append(text);

                if (_prefixDelimiters.length > 1) {
                    sb.append(_prefixDelimiters[1]);
                }
            }

            return sb.toString();
        }
    }

    /**
     * Actually writes the message - to be implemented by the various Writer subclasses
     */
    protected abstract void _write(final String message);
}
