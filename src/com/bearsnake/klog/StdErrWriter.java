package com.bearsnake.klog;

public class StdErrWriter extends Writer {

    @Override
    public void _write(
        final String text
    ) {
        System.err.println(text);
    }
}
