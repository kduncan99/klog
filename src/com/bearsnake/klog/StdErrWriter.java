// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

public class StdErrWriter extends Writer {

    /**
     * Creates a new StdErrWriter with a specific level mask value.
     * @param levelMask mask indicating the levels which this writer will allow to be persisted
     */
    public StdErrWriter(
        final LevelMask levelMask
    ) {
        super(levelMask);
        setPrefixDelimiter(':');
        addPrefixEntity(PrefixEntity.LOGGER_NAME);
        addPrefixEntity(PrefixEntity.LEVEL, PrefixEntityWidthSpecifier.FIXED, 5);
    }

    /**
     * Creates a new StdErrWriter with a level threshold value
     * @param level indicates the level threshold for this writer
     */
    public StdErrWriter(
        final Level level
    ) {
        this(new LevelMask(level));
    }

    @Override
    protected void _write(
        final String text
    ) {
        System.err.println(text);
    }
}
