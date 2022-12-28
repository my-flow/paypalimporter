package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

public final class LogFormatterTest {

    @Test
    public void testFormatLogRecord() {
        LogFormatter logFormatter = new LogFormatter();

        LogRecord logRecord = new LogRecord(Level.CONFIG, "stub message");
        assertThat(logFormatter.format(logRecord), notNullValue());

        logRecord.setThrown(new IOException("test exception"));
        assertThat(logFormatter.format(logRecord), notNullValue());
    }

}
