// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.modules.features.paypalimporter.bootstrap.MainHelper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

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

    private final CoreModule coreModule;
    private final CoreComponent coreComponent;
    private final MainHelper mainHelper;

    /**
     * Standard constructor must be available in the Moneydance context.
     */
    public Main() {
        super();
        this.coreModule = new CoreModule(this);
        this.coreComponent = DaggerCoreComponent.builder().coreModule(this.coreModule).build();
        this.mainHelper = this.coreComponent.mainHelper();
    }

    @Override
    public void init() {
        this.coreModule.setContext(this.getContext());
        this.mainHelper.init(this.coreComponent, this);

        // register this module to be invoked via the application toolbar
        LOG.config("Registering toolbar feature");
        Localizable localizable = this.coreComponent.localizable();
        this.getContext().registerFeature(
                this,
                this.coreComponent.settings().getStartWizardSuffix(),
                null, // buttonImage
                localizable.getLabelButtonText());
    }

    @Override
    public String getName() {
        return this.mainHelper.getName();
    }

    @Override
    public Image getIconImage() {
        return this.mainHelper.getIconImage();
    }

    @Override
    public void invoke(final String uri) {
        this.mainHelper.invoke(uri);
    }

    @Override
    public void update(final Observable observable, final Object updateAll) {
        this.coreModule.setContext(this.getContext());
        this.mainHelper.update(this.coreComponent);
    }

    @Override
    public void unload() {
        this.mainHelper.unload();
    }

    @Override
    public void cleanup() {
        this.mainHelper.cleanup();
    }
}
