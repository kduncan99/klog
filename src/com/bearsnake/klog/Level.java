// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

/**
 * Indicates a particular logging level.
 * Higher priority levels have lower-numbered ordinals.
 * Intended to be used in conjunction with the LevelMask class.
 */
public enum Level {
    FATAL("FATAL"),
    ERROR("ERROR"),
    WARNING("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG"),
    TRACE("TRACE");

    private final String _token;

    Level(final String token) { _token = token; }

    /**
     * Retrieves a value with a bit set in the position corresponding to the ordinal of the enum value
     */
    public int getBit() { return 1 << ordinal(); }

    /**
     * Returns a value with a bit set in the position corresponding to the ordinal of the enum value,
     * and to all the enum values of a higher priority.
     */
    public int getPriorityBits() { return (getBit() << 1) - 1; }

    @Override
    public String toString() { return _token; }
}
