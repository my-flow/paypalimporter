// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;
import com.moneydance.modules.features.paypalimporter.controller.ViewControllerImpl;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.modules.features.paypalimporter.util.Tracker;

import javax.annotation.Nullable;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * The main class of the extension, instantiated by Moneydance's class loader.
 *
 * @author Florian J. Breunig
 */
@SuppressWarnings("nullness")
public final class Main extends FeatureModule implements Observer {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private final Preferences prefs;
    private final Settings settings;
    @Nullable private Tracker tracker;
    @Nullable private ViewController viewController;

    static {
        Helper.loadLoggerConfiguration();
    }

    /**
     * Standard constructor must be available in the Moneydance context.
     */
    public Main() {
        super();
        LOG.info("Initializing extension in Moneydance's application context.");
        this.prefs = Helper.INSTANCE.getPreferences();
        this.settings = Helper.INSTANCE.getSettings();
    }

    @Override
    public void init() {
        Helper.INSTANCE.addObserver(this);
        this.tracker = Helper.INSTANCE.getTracker(this.getBuild());

        if (this.prefs.isFirstRun()) {
            this.prefs.setFirstRun(false);
            LOG.config("Install");
            this.tracker.track(Tracker.EventName.INSTALL);

            // show wizard immediately after installation
            this.getViewController().startWizard();
        }

        // register this module to be invoked via the application toolbar
        LOG.config("Registering toolbar feature");
        Localizable localizable = Helper.INSTANCE.getLocalizable();
        this.getContext().registerFeature(
                this,
                this.settings.getStartWizardSuffix(),
                null, // buttonImage
                localizable.getLabelButtonText());
    }

    @Override
    public String getName() {
        return this.settings.getExtensionName();
    }

    @Override
    public Image getIconImage() {
        return this.settings.getIconImage();
    }

    @Override
    public void invoke(final String uri) {
        LOG.config(String.format("invoke %s", uri));
        if (this.settings.getStartWizardSuffix().equals(uri)) {
            this.getViewController().startWizard();
        }
    }

    @Override
    public void update(final Observable observable, final Object updateAll) {
        LOG.info("Reloading context from underlying framework.");
        Helper.INSTANCE.setContext(this.getContext());
    }

    @Override
    public void unload() {
        LOG.info("Unloading extension.");
        this.tracker.track(Tracker.EventName.UNINSTALL);
        this.cleanup();
        this.prefs.setAllWritablePreferencesToNull();
    }

    @Override
    public void cleanup() {
        if (this.viewController != null) {
            this.viewController.cancel();
        }
    }

    /**
     * Lazy initialization.
     *
     * @return the single instance of the view controller.
     */
    private ViewController getViewController() {
        if (this.viewController == null) {
            this.viewController = new ViewControllerImpl(
                    this.getContext(),
                    this.tracker);
        }
        return this.viewController;
    }
}
