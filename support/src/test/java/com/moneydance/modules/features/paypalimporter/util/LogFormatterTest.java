// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
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
