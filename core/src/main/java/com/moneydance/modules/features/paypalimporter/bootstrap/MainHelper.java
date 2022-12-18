package com.moneydance.modules.features.paypalimporter.bootstrap;

import com.moneydance.modules.features.paypalimporter.CoreComponent;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.awt.Image;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * The main class of the extension, instantiated by Moneydance's class loader.
 *
 * @author Florian J. Breunig
 */
@SuppressWarnings("nullness")
public final class MainHelper {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(MainHelper.class.getName());

    private final Settings settings;
    private CoreComponent coreComponent;
    private Preferences prefs;
    private ViewController viewController;

    MainHelper(final Settings argSettings) {
        super();
        Helper.loadLoggerConfiguration(argSettings);
        LOG.info("Initializing extension in Moneydance's application context.");
        this.settings = argSettings;
    }

    public void init(final CoreComponent argCoreComponent, final Observer argObserver) {
        Helper.INSTANCE.init(argCoreComponent);
        Helper.INSTANCE.addObserver(argObserver);

        this.coreComponent = argCoreComponent;

        if (argCoreComponent.context().getCurrentAccountBook() != null) {
            Preferences preferences = this.getPrefs();
            if (preferences.isFirstRun()) {
                preferences.setFirstRun(false);
                LOG.config("Install");

                // show wizard immediately after installation
                this.getViewController().startWizard();
            }
        }
    }

    public String getName() {
        return this.settings.getExtensionName();
    }

    public Image getIconImage() {
        return this.settings.getIconImage();
    }

    public void invoke(final String uri) {
        LOG.config(String.format("invoke %s", uri));
        if (this.settings.getStartWizardSuffix().equals(uri)) {
            this.getViewController().startWizard();
        }
    }

    public void update(final CoreComponent argCoreComponent) {
        Helper.INSTANCE.init(argCoreComponent);
    }

    public void unload() {
        LOG.info("Unloading extension."); //$NON-NLS-1$
        this.cleanup();
        this.getPrefs().setAllWritablePreferencesToNull();
    }

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
            this.viewController = this.coreComponent.viewController();
        }
        return this.viewController;
    }

    /**
     * Lazy initialization.
     *
     * @return the single instance of the preferences.
     */
    private Preferences getPrefs() {
        if (this.prefs == null) {
            this.prefs = this.coreComponent.preferences();
        }
        return this.prefs;
    }
}
