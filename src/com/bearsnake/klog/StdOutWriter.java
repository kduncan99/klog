// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

public class StdOutWriter extends Writer {

    /**
     * Creates a new StdOutWriter with a specific level mask value.
     * @param levelMask mask indicating the levels which this writer will allow to be persisted
     */
    public StdOutWriter(
        final LevelMask levelMask
    ) {
        super(levelMask);
        setPrefixDelimiter(':');
        addPrefixEntity(PrefixEntity.LOGGER_NAME);
        addPrefixEntity(PrefixEntity.LEVEL, PrefixEntityWidthSpecifier.FIXED, 5);
    }

    /**
     * Creates a new StdOutWriter with a level threshold value
     * @param level indicates the level threshold for this writer
     */
    public StdOutWriter(
        final Level level
    ) {
        super(new LevelMask(level));
        setPrefixDelimiter(':');
        addPrefixEntity(PrefixEntity.LOGGER_NAME);
        addPrefixEntity(PrefixEntity.LEVEL, PrefixEntityWidthSpecifier.FIXED, 5);
    }

    @Override
    protected void _write(
        final String text
    ) {
        System.out.println(text);
    }
}
