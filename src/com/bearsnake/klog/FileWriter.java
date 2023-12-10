// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

import java.io.IOException;
import java.io.PrintStream;

public class FileWriter extends Writer {

    private final boolean _buffered;
    private PrintStream _stream;

    /**
     * Creates a new FileWriter with a specific level mask value.
     * @param levelMask mask indicating the levels which this writer will allow to be persisted
     * @param fileName name of the log file to be written
     * @param buffered true if output to the log file is buffered (lazy-written), false to flush each entry
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

        _buffered = buffered;
        try {
            _stream = new PrintStream(fileName);
        } catch (IOException ex) {
            System.err.println("Cannot create FileWriter for file " + fileName);
            _stream = null;
        }
    }

    /**
     * Creates a new FileWriter with a level threshold value
     * @param level indicates the level threshold for this writer
     * @param fileName name of the log file to be written
     * @param buffered true if output to the log file is buffered (lazy-written), false to flush each entry
     */
    public FileWriter(
        final Level level,
        final String fileName,
        final boolean buffered
    ) {
        this(new LevelMask(level), fileName, buffered);
    }

    @Override
    public synchronized void close(
        final Logger logger
    ) {
        write(logger, Level.INFO, null, "Closing log");
        _stream.close();
    }

    @Override
    public void _write(
        final String text
    ) {
        _stream.println(text);
        if (!_buffered) {
            _stream.flush();
        }
    }
}
