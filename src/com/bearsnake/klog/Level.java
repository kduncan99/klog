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

    @Override
    public String toString() { return _token; }
}
