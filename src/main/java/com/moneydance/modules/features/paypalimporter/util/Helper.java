// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.LogManager;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.Validate;

/**
 * This singleton provides public convenience methods.
 *
 * @author Florian J. Breunig
 */
public enum Helper {

    /**
     * Helper instance.
     */
    INSTANCE;

    private static final KeyStroke ESCAPE_STROKE =
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    private static final String DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY =
            "com.spodding.tackline.dispatch:WINDOW_CLOSING";

    private final HelperObservable observable;
    private final Settings settings;
    private final Preferences prefs;
    private       Localizable localizable;
    private       Tracker tracker;

    private Helper() {
        this.observable = new HelperObservable();
        this.settings   = new Settings();
        this.prefs      = new Preferences();
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Preferences getPreferences() {
        return this.prefs;
    }

    public Localizable getLocalizable() {
        synchronized (Helper.class) {
            if (this.localizable == null) {
                this.localizable = new Localizable(
                        this.settings.getLocalizableResource(),
                        this.prefs.getLocale());
            }
        }
        return this.localizable;
    }

    public Tracker getTracker(final int build) {
        synchronized (Helper.class) {
            if (this.tracker == null) {
                this.tracker = new Tracker(
                        build,
                        this.settings.getExtensionName(),
                        this.prefs.getFullVersion(),
                        this.settings.getTrackingCode());
            }
        }
        return this.tracker;
    }

    public void addObserver(final Observer o) {
        this.observable.addObserver(o);
    }

    public void setChanged() {
        this.observable.setChanged();
    }

    public void notifyObservers(final Object arg) {
        this.observable.notifyObservers(arg);
    }

    public void setContext(final FeatureModuleContext context) {
        this.prefs.setContext(context);
    }

    public static void loadLoggerConfiguration() {
        try {
            InputStream inputStream = getInputStreamFromResource(
                    Helper.INSTANCE.getSettings()
                    .getLoggingPropertiesResource());
            LogManager.getLogManager().readConfiguration(inputStream);

        } catch (SecurityException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static InputStream getInputStreamFromResource(
            final String resource) {
        ClassLoader cloader     = Helper.class.getClassLoader();
        InputStream inputStream = cloader.getResourceAsStream(resource);
        Validate.notNull(inputStream, "Resource %s was not found.",  resource);
        return inputStream;
    }

    public static void installEscapeCloseOperation(final JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent event) {
                dialog.dispatchEvent(
                        new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                ESCAPE_STROKE, DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY);
        root.getActionMap().put(
                DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY, dispatchClosing);
    }

    /**
     * @author Florian J. Breunig
     */
    private final class HelperObservable extends Observable {
        @Override
        public synchronized void setChanged() { // increase visibility
            super.setChanged();
        }
    }
}
