package com.bearsnake;

import com.bearsnake.klog.Level;
import com.bearsnake.klog.Logger;
import com.bearsnake.klog.PrefixEntity;
import com.bearsnake.klog.StdErrWriter;
import com.bearsnake.klog.StdOutWriter;

public class Main {

    public static void main(final String[] args) {
        var console = new StdOutWriter(Level.INFO);

        var err = new StdErrWriter(Level.ERROR);
        err.clearPrefixEntities();
        err.addPrefixEntity(PrefixEntity.DATE_AND_TIME);
        err.addPrefixEntity(PrefixEntity.SOURCE_PACKAGE);
        err.addPrefixEntity(PrefixEntity.SOURCE_CLASS);
        err.addPrefixEntity(PrefixEntity.SOURCE_METHOD);
        err.addPrefixEntity(PrefixEntity.SOURCE_FILE_NAME);
        err.addPrefixEntity(PrefixEntity.SOURCE_LINE_NUMBER);

        var fee = new Logger("Fee", Level.TRACE);
        fee.addWriter(console);
        fee.addWriter(err);

        var b = new byte[125];
        fee.writeBuffer(Level.INFO, "All the world is a stage".getBytes());
    }
}
