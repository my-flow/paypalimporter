/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.util;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.Validate;

import com.moneydance.apps.md.controller.FeatureModuleContext;

/**
 * This helper class provides public convenience methods.
 *
 * @author Florian J. Breunig
 */
public enum Helper {

    /**
     * Helper instance.
     */
    INSTANCE;

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(Helper.class.getName());

    private static final KeyStroke ESCAPE_STROKE =
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    private static final String DISPATCH_WINDOW_CLOSING_ACTION_MAP_KEY =
            "com.spodding.tackline.dispatch:WINDOW_CLOSING";

    private final HelperObservable observable;
    private       Preferences prefs;
    private       Tracker tracker;

    private Helper() {
        this.observable = new HelperObservable();
    }

    public Preferences getPreferences() {
        synchronized (Helper.class) {
            if (this.prefs == null) {
                this.prefs = new Preferences();
            }
        }
        return this.prefs;
    }

    public Settings getSettings() {
        return Settings.INSTANCE;
    }

    public Localizable getLocalizable() {
        return Localizable.INSTANCE;
    }

    public Tracker getTracker(final int build) {
        synchronized (Helper.class) {
            if (this.tracker == null) {
                this.tracker = new Tracker(build);
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
        this.getPreferences().setContext(context);
        this.getLocalizable().update();
    }

    public void loadLoggerConfiguration() {
        try {
            InputStream inputStream = this.getInputStreamFromResource(
                    this.getSettings().getLoggingPropertiesResource());
            LogManager.getLogManager().readConfiguration(inputStream);

        } catch (SecurityException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public InputStream getInputStreamFromResource(
            final String resource) {
        ClassLoader cloader     = Helper.class.getClassLoader();
        InputStream inputStream = cloader.getResourceAsStream(resource);
        Validate.notNull(inputStream, "Resource %s was not found.",  resource);
        return inputStream;
    }

    public Image getIconImage() {
        Image image = null;
        try {
            LOG.config(String.format("Loading icon %s from resource.",
                    this.getSettings().getIconResource()));
            InputStream inputStream =
                    Helper.INSTANCE.getInputStreamFromResource(
                            this.getSettings().getIconResource());
            image = ImageIO.read(inputStream);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return image;
    }

    public Image getHelpImage() {
        Image image = null;
        try {
            LOG.config(String.format("Loading icon %s from resource.",
                    this.getSettings().getHelpResource()));
            InputStream inputStream =
                    Helper.INSTANCE.getInputStreamFromResource(
                            this.getSettings().getHelpResource());
            image = ImageIO.read(inputStream);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return image;
    }

    public void installEscapeCloseOperation(final JDialog dialog) {
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
        public synchronized void setChanged() { // increase visiblity
            super.setChanged();
        }
    }
}
