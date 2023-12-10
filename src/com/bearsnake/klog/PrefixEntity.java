// klog project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.klog;

/**
 * Indicates a particular datum to be included in the prefix of any log entry output.
 * {@link #LEVEL}
 * {@link #LOGGER_NAME}
 * {@link #DATE_AND_TIME}
 * {@link #SOURCE_PACKAGE}
 * {@link #SOURCE_CLASS}
 * {@link #SOURCE_METHOD}
 * {@link #SOURCE_FILE_NAME}
 * {@link #SOURCE_LINE_NUMBER}
 */
public enum PrefixEntity {
    /**
     * The logging level associated with the log entry
     */
    LEVEL,

    /**
     * The name associated with the logger which passed the log entry to the writer
     */
    LOGGER_NAME,

    /**
     * The date and time the log entry was created
     */
    DATE_AND_TIME,

    /**
     * The package containing the client code which posted the log entry
     */
    SOURCE_PACKAGE,

    /**
     * The name of the class which posted the log entry
     */
    SOURCE_CLASS,

    /**
     * The name of the method which posted the log entry
     */
    SOURCE_METHOD,

    /**
     * The file name containing the source code which posted the log entry
     */
    SOURCE_FILE_NAME,

    /**
     * The line number of the source code which posted the log entry
     */
    SOURCE_LINE_NUMBER,
}
