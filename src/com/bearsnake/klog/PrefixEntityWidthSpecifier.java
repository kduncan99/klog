// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

/**
 * Indicates how (and if) the output of a particular prefix entity is to be limited.
 * Except in the case of NONE, this enum will be accompanied by an integer indicating a number of columns.
 * {@link #FIXED}
 * {@link #MAXIMUM}
 * {@link #MINIMUM}
 * {@link #NONE}
 */
public enum PrefixEntityWidthSpecifier {
    /**
     * No limit on the width
     */
    NONE,

    /**
     * The output is always a fixed number of columns
     */
    FIXED,

    /**
     * The output cannot exceed the indicated number of columns
     */
    MAXIMUM,

    /**
     * The output will always be at leaast the indicated number of columns
     */
    MINIMUM,
}
