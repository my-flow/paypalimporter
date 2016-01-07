// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.modules.features.paypalimporter.integration.OnlineServiceFactory;
import com.moneydance.modules.features.paypalimporter.integration.PayPalOnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

/**
 * This preferences class contains the values the user can control in the
 * application. It serves as a facade abstracting Moneydance's
 * <code>UserPreferences</code> (received from the
 * <code>FeatureModuleContext</code>).
 *
 * @author Florian J. Breunig
 */
public final class Preferences {

    private static final String KEY_FIRST_RUN = "paypalimporter.first_run";

    private final IAccountBookFactory accountBookFactory;

    private UserPreferences userPreferences;
    private IAccountBook accountBook;
    private PayPalOnlineService profile;

    Preferences(final IAccountBookFactory argAccountBookFactory) {
        this.accountBookFactory = argAccountBookFactory;
    }

    void setContext(final FeatureModuleContext context) {
        this.userPreferences = ((com.moneydance.apps.md.controller.Main)
                context).getPreferences();
        this.accountBook = this.accountBookFactory.createAccountBook(context);
        if (this.accountBook != null) {
            this.profile = OnlineServiceFactory.createService(this.accountBook);
        }
    }

    private UserPreferences getUserPreferences() {
        if (this.userPreferences == null) {
            Helper.INSTANCE.setChanged();
            Helper.INSTANCE.notifyObservers(Boolean.FALSE);
            Validate.notNull(this.userPreferences,
                    "user preferences not initialized");
        }
        return this.userPreferences;
    }

    private IAccountBook getAccountBook() {
        if (this.accountBook == null) {
            Helper.INSTANCE.setChanged();
            Helper.INSTANCE.notifyObservers(Boolean.FALSE);
            Validate.notNull(this.accountBook, "Account book not initialized");
        }
        return this.accountBook;
    }

    private PayPalOnlineService getPayPalOnlineService() {
        if (this.profile == null) {
            Helper.INSTANCE.setChanged();
            Helper.INSTANCE.notifyObservers(Boolean.FALSE);
            Validate.notNull(this.profile,
                    "PayPal online service not initialized");
        }
        return this.profile;
    }

    public void setAllWritablePreferencesToNull() {
        this.getUserPreferences().setSetting(KEY_FIRST_RUN, (String) null);
        OnlineServiceFactory.removeService(this.getAccountBook());
    }

    public void setFirstRun(final boolean firstRun) {
        this.getUserPreferences().setSetting(KEY_FIRST_RUN, firstRun);
    }

    public boolean isFirstRun() {
        return this.getUserPreferences().getBoolSetting(KEY_FIRST_RUN, true);
    }

    public String getFullVersion() {
        final String fullString = this.getUserPreferences().getSetting(
                "current_version", "0");
        return fullString;
    }

    public Locale getLocale() {
        return this.getUserPreferences().getLocale();
    }

    public boolean hasProxy() {
        return this.getUserPreferences().getBoolSetting(
                UserPreferences.NET_USE_PROXY,
                false);
    }

    public String getProxyHost() {
        return this.getUserPreferences().getSetting(
                UserPreferences.NET_PROXY_HOST);
    }

    public int getProxyPort() {
        return this.getUserPreferences().getIntSetting(
                UserPreferences.NET_PROXY_PORT,
                0);
    }

    public boolean hasProxyAuthentication() {
        return this.getUserPreferences().getBoolSetting(
                UserPreferences.NET_AUTH_PROXY,
                false);
    }

    public String getProxyUsername() {
        return this.getUserPreferences().getSetting(
                UserPreferences.NET_PROXY_USER);
    }

    public String getProxyPassword() {
        return this.getUserPreferences().getSetting(
                UserPreferences.NET_PROXY_PASS);
    }

    public void assignBankingFI(final int accountId) {
        this.getPayPalOnlineService().assignToAccount(
                this.getAccountBook(), accountId);
    }

    public void setUsername(final int accountId, final String username) {
        this.getPayPalOnlineService().setUsername(accountId, username);
    }

    public String getUsername(final int accountId) {
        return this.getPayPalOnlineService().getUsername(accountId);
    }

    public void setPassword(final int accountId, final char[] password) {
        this.getPayPalOnlineService().setPassword(accountId, password);
    }

    public char[] getPassword(final int accountId) {
        return this.getPayPalOnlineService().getPassword(accountId);
    }

    public void setSignature(final int accountId, final String signature) {
        this.getPayPalOnlineService().setSignature(accountId, signature);
    }

    public String getSignature(final int accountId) {
        return this.getPayPalOnlineService().getSignature(accountId);
    }

    public boolean hasUsedCombination(
            final int accountId,
            final String username) {

        return username != null && username.equals(
                this.getPayPalOnlineService().getUsername(accountId));
    }
}
