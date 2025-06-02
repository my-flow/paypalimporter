package com.moneydance.modules.features.paypalimporter.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.jcip.annotations.Immutable;

@Immutable
public final class LogFormatter extends Formatter {

    private static final String LINE_SEPARATOR =
            System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        String logEntry = String.format("%s %s: %s%s",
            new Date(record.getMillis()),
            record.getLevel().getLocalizedName(),
            this.formatMessage(record),
            LINE_SEPARATOR);

        if (record.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                record.getThrown().printStackTrace(printWriter);
                logEntry += stringWriter.toString();
            }
        }

        return logEntry;
    }
}
