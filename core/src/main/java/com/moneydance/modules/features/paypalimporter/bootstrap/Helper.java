// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.bootstrap;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.modules.features.paypalimporter.CoreComponent;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;

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
@SuppressWarnings("nullness")
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
    private CoreComponent coreComponent;

    Helper() {
        this.observable = new HelperObservable();
    }

    public FeatureModuleContext getContext() {
        return this.coreComponent.context();
    }

    void init(final CoreComponent argCoreComponent) {
        synchronized (Helper.class) {
            this.coreComponent = argCoreComponent;
        }
    }

    public Preferences getPreferences() {
        return this.coreComponent.preferences();
    }

    public Localizable getLocalizable() {
        return this.coreComponent.localizable();
    }

    public void addObserver(final Observer observer) {
        this.observable.addObserver(observer);
    }

    public void setChanged() {
        this.observable.setChanged();
    }

    public void notifyObservers(final Object arg) {
        this.observable.notifyObservers(arg);
    }

    public static void loadLoggerConfiguration(final Settings settings) {
        try {
            InputStream inputStream = getInputStreamFromResource(
                    settings.getLoggingPropertiesResource());
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (SecurityException | IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static InputStream getInputStreamFromResource(
            final String resource) {
        ClassLoader cloader = Helper.class.getClassLoader();
        InputStream inputStream = cloader.getResourceAsStream(resource);
        Validate.notNull(inputStream, "Resource %s was not found.", resource);
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
    private static final class HelperObservable extends Observable {
        @Override
        public synchronized void setChanged() { // increase visibility
            super.setChanged();
        }
    }
}
