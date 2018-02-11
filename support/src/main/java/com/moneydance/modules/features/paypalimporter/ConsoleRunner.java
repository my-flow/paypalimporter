// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.util.UiUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to run the extension as a stand-alone application from
 * the console or from an IDE. It allows for fast feedback during the
 * development process.
 *
 * @author Florian J. Breunig
 */
final class ConsoleRunner {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG =
            Logger.getLogger(ConsoleRunner.class.getName());

    static {
        Helper.loadLoggerConfiguration();
    }

    /**
     * Restrictive constructor.
     */
    private ConsoleRunner() {
        // Prevents this class from being instantiated from the outside.
    }

    /**
     * This method is called directly from the console.
     */
    public static void main(final String[] args) {

        for (String arg : args) {
            if ("-d".equals(arg)) {
                LOG.warning("debugging...");
                com.moneydance.apps.md.controller.Main.DEBUG = true;
                continue;
            }
            LOG.warning(String.format("ignoring argument: %s", arg));
        }

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        UiUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    static void createAndShowGUI() {
        final Main main = new Main();
        final StubContextFactory factory = new StubContextFactory(main);
        factory.init();
        main.init();

        try {
            main.invoke(Helper.INSTANCE.getSettings().getStartWizardSuffix());
        } catch (Exception e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.WARNING, message, e);
            }
        }
    }
}
