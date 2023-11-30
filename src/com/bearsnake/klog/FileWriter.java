package com.bearsnake.klog;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWriter extends Writer {

    private boolean _flush;
    private PrintStream _stream;

    public FileWriter(
        final String fileName,
        final boolean flush
    ) {
        try {
            _stream = new PrintStream(new File(fileName));
        } catch (IOException ex) {
            System.err.println("Cannot create FileWriter for file " + fileName);
            _stream = null;
        }
    }

    @Override
    public void close() {
        if (_stream != null) {
            _stream.close();
        }
    }

    @Override
    public void _write(
        final String text
    ) {
        var dt = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        _stream.println(dt + ":" + text);
        if (_flush) {
            _stream.flush();
        }
    }
}
