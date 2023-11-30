// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

/**
 * Contains a set of bits, each of which corresponds to a particular Level value.
 * Higher-priority levels are on the right, with the highest priority in the right-most bit.
 */
public class LevelMask {

    public static final int ALL = 0x7FFFFFFF;
    public static final int NONE = 0x0;
    public static final int DEBUG = (1 << Level.DEBUG.ordinal());
    public static final int ERROR = (1 << Level.ERROR.ordinal());
    public static final int FATAL = (1 << Level.FATAL.ordinal());
    public static final int INFO = (1 << Level.INFO.ordinal());
    public static final int WARNING = (1 << Level.WARNING.ordinal());
    public static final int TRACE = (1 << Level.TRACE.ordinal());

    private int _bitMask = NONE;

    /**
     * Creates a new LevelMask with no bits sit.
     * This is equivalent to LevelMask.NONE.
     */
    public LevelMask() {}

    /**
     * Creates a new LevelMask with all bits set for levels which are at or higher than
     * the priority of the indicated level. Thus, providing Level.ERROR would result in
     * bits set for both Level.ERROR and Level.FATAL.
     * Note that lower bit values correspond to higher level priorities - the highest priority
     * level corresponds to bit 0, which is value 0x01.
     * @param level indicates the level threshold
     */
    public LevelMask(
        final Level level
    ) {
        _bitMask = (level.ordinal() << 1) - 1;
    }

    /**
     * Creates a new LevelMask with bits set according to the given bit mask
      * @param bitMask indicates the bits to be set
     */
    public LevelMask(
        final int bitMask
    ) {
        _bitMask = bitMask;
    }

    /**
     * Clears all the bits of the internal bitmask
     * @return this object
     */
    public LevelMask clear() {
        _bitMask = 0;
        return this;
    }

    /**
     * Clears the bit within the internal bitmask which corresponds to the given level
     * @param level indicates the bit to be cleared
     * @return this object
     */
    public LevelMask clearBit(
        final Level level
    ) {
        return clearBits(1 << level.ordinal());
    }

    /**
     * Clears the indicated bits within the internal bitmask, leaving all other bits alone.
     * @param bitMask any 1 values in this bit mask will produce 0 values in the corresponding internal bitmask
     * @return this object
     */
    public LevelMask clearBits(
        final int bitMask
    ) {
        _bitMask &= ~bitMask;
        return this;
    }

    /**
     * Sets the internal bitmask to the given value
     * @param bitMask the value to be set
     * @return this object
     */
    public LevelMask set(
        final int bitMask
    ) {
        _bitMask = bitMask;
        return this;
    }

    /**
     * Sets the bit which corresponds to the given level, in the internal bitmask.
     * @param level indicates the bit to be set
     * @return this object
     */
    public LevelMask setBit(
        final Level level
    ) {
        return setBits(1 << level.ordinal());
    }

    /**
     * Sets the bits which are indicated in the given bitmask, in the internal bitmask
     * without clearing any existing bits.
     * @param bitMask indicates (additional) bits to be set
     * @return this object
     */
    public LevelMask setBits(
        final int bitMask
    ) {
        _bitMask |= bitMask;
        return this;
    }

    /**
     * For a given level value, this method indicates whether the level matches the current bitmask.
     * I.e., returns true if the internal bit which corresponds to the given level, is set.
     * @param level the level to be checked
     * @return true if the bit is set, else false
     */
    public boolean matches(
        final Level level
    ) {
        return (level.ordinal() & _bitMask) != 0;
    }
}
