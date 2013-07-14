// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * @author Florian J. Breunig
 */
public final class LogFormatter extends Formatter {

    private static final String LINE_SEPARATOR =
            System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(new Date(record.getMillis()))
        .append(' ')
        .append(record.getLevel().getLocalizedName())
        .append(": ")
        .append(this.formatMessage(record))
        .append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            record.getThrown().printStackTrace(printWriter);
            printWriter.close();
            stringBuilder.append(stringWriter.toString());
        }

        return stringBuilder.toString();
    }
}
