package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.moneydance.apps.md.extensionapi.AccountEditor;
import com.moneydance.apps.md.view.HomePageView;

import javax.annotation.Nullable;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This test stub simulates a context in stand-alone mode. It provides canned
 * answers to the standard calls. Also, it can be cast safely to
 * <code>com.moneydance.apps.md.controller.Main</code> in order to request the
 * user's preferences.
 */
public final class StubContext extends Main {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG =
            Logger.getLogger(StubContext.class.getName());

    @Nullable private final FeatureModule featureModule;
    private final StubAccountBook accountBook;
    @Nullable private UserPreferences userPreferences;

    @SuppressWarnings("nullness")
    public StubContext(@Nullable final FeatureModule argFeatureModule,
            final StubAccountBook argAccountBook) {
        super();
        this.featureModule = argFeatureModule;
        this.accountBook = argAccountBook;
        try {
            if (!this.isInitialized()) {
                this.initializeApp();
            }
        } catch (Error e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.SEVERE, message, e);
            }
        } catch (Exception e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.WARNING, message, e);
            }
        }
    }

    @Override
    public AccountBook getCurrentAccountBook() {
        return this.accountBook.getWrappedOriginal();
    }

    public StubAccountBook getAccountBook() {
        return this.accountBook;
    }

    @Override
    public Account getRootAccount() {
        return this.accountBook.getRootAccount();
    }

    @Override
    public String getVersion() {
        return "0";
    }

    @Override
    public int getBuild() {
        return 0;
    }

    @Override
    public void showURL(final String url) {
        if (this.featureModule != null) {
            String suffix = url;
            final int theIdx = url.lastIndexOf(':');
            if (theIdx >= 0) {
                suffix = url.substring(theIdx + 1);
            }
            LOG.config(String.format(
                    "Stub context forwards received URL %s to module %s",
                    suffix,
                    this.featureModule));
            this.featureModule.invoke(suffix);
        }
    }

    @Override
    public void registerFeature(
            final FeatureModule module,
            final String parameters,
            final Image buttonImage,
            final String buttonText) {
        LOG.config(String.format(
                "Stub context ignores registered feature %s of module %s",
                buttonText,
                module));
    }

    @Override
    public void registerHomePageView(
            final FeatureModule module,
            final HomePageView view) {
        LOG.config(String.format(
                "Stub context ignores registered homepage view %s of module %s",
                view,
                module));
    }

    @Override
    public void registerAccountEditor(
            final FeatureModule module,
            final int accountType,
            final AccountEditor editor) {
        LOG.config(String.format(
                "Stub context ignores registered account editor %s of module %s",
                editor,
                module));
    }

    @Override
    public UserPreferences getPreferences() {
        if (this.userPreferences == null) {
            final File preferencesFile = Common.getPreferencesFile();
            this.userPreferences       = new UserPreferences(preferencesFile);
            LOG.config(String.format(
                    "Stub context returns user preferences from file %s",
                    preferencesFile.getAbsolutePath()));
        }
        assert this.userPreferences != null : "@AssumeAssertion(nullness)";
        return this.userPreferences;
    }
}
