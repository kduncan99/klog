package com.bearsnake.klog;

public class StdOutWriter extends Writer {

    @Override
    public void _write(
        final String text
    ) {
        System.out.println(text);
    }
}
