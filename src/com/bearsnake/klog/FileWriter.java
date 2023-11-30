// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class FileWriter extends Writer {

    private final List<Logger> _clientLoggers = new LinkedList<>();
    private final boolean _buffered;
    private PrintStream _stream;

    /**
     * Creates a new FileWriter with a specific level mask value.
     * @param levelMask mask indicating the levels which this writer will allow to be persisted
     */
    public FileWriter(
        final LevelMask levelMask,
        final String fileName,
        final boolean buffered
    ) {
        super(levelMask);
        setPrefixDelimiter(':');
        addPrefixEntity(PrefixEntity.LOGGER_NAME);
        addPrefixEntity(PrefixEntity.LEVEL, PrefixEntityWidthSpecifier.FIXED, 5);
        addPrefixEntity(PrefixEntity.CATEGORY);

        _buffered = buffered;
        try {
            _stream = new PrintStream(fileName);
        } catch (IOException ex) {
            System.err.println("Cannot create FileWriter for file " + fileName);
            _stream = null;
        }
    }

    @Override
    public synchronized void close(
        final Logger logger
    ) throws IOException {
        if (!_clientLoggers.contains(logger)) {
            throw new IOException("Logger attempted to close writer which it did not open, or has already closed.");
        }

        write(logger, Level.INFO, null, "Closing log");
        _clientLoggers.remove(logger);
        if (!isOpen()) {
            _stream.close();
        }
    }

    @Override
    public synchronized boolean isOpen() {
        return !_clientLoggers.isEmpty();
    }

    @Override
    public synchronized void open(
        final Logger logger
    ) throws IOException {
        if (_clientLoggers.contains(logger)) {
            throw new IOException("Logger attempted to open writer more than once");
        }

        _clientLoggers.add(logger);
        write(logger, Level.INFO, null, "Log opened");
    }

    @Override
    public void _write(
        final String text
    ) {
        if (isOpen()) {
            _stream.println(text);
            if (!_buffered) {
                _stream.flush();
            }
        }
    }
}
