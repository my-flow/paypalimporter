// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.bootstrap.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;
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
        final Main main = new Main();
        final CoreModule coreModule = new CoreModule(main);
        final CoreComponent coreComponent = DaggerCoreComponent.builder().coreModule(coreModule).build();

        Helper.loadLoggerConfiguration(coreComponent.settings());

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
                createAndShowGUI(main, coreComponent.settings());
            }
        });
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    static void createAndShowGUI(
            final FeatureModule featureModule,
            final Settings settings) {
        final StubContextFactory factory = new StubContextFactory(featureModule);
        factory.init();
        featureModule.init();

        try {
            featureModule.invoke(settings.getStartWizardSuffix());
        } catch (Exception e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.WARNING, message, e);
            }
        }
    }
}
