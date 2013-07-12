/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.util;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.integration.OnlineServiceFactory;
import com.moneydance.modules.features.paypalimporter.integration.PayPalOnlineService;

/**
 * This preferences class contains the values the user can control in the
 * application. It serves as a facade abstracting Moneydance's
 * <code>UserPreferences</code> (received from the
 * <code>FeatureModuleContext</code>).
 *
 * @author Florian J. Breunig
 */
public final class Preferences {

    private UserPreferences userPreferences;
    private RootAccount rootAccount;
    private PayPalOnlineService profile;

    void setContext(final FeatureModuleContext context) {
        this.userPreferences = ((com.moneydance.apps.md.controller.Main)
                context).getPreferences();
        this.rootAccount = context.getRootAccount();
        if (this.rootAccount != null) {
            this.profile = OnlineServiceFactory.getService(this.rootAccount);
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
        this.getUserPreferences().setSetting(
                "paypal.first_run",
                (String) null);
        OnlineServiceFactory.removeService(this.rootAccount);
    }

    public void setFirstRun(final boolean firstRun) {
        this.getUserPreferences().setSetting(
                "paypal.first_run",
                firstRun);
    }

    public boolean isFirstRun() {
        return this.getUserPreferences().getBoolSetting(
                "paypal.first_run",
                true);
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
                this.rootAccount, accountId);
    }

    public void setUsername(final String username) {
        this.getPayPalOnlineService().setUsername(username);
    }

    public String getUsername() {
        return this.getPayPalOnlineService().getUsername();
    }

    public void setPassword(final char[] password) {
        this.getPayPalOnlineService().setPassword(password);
    }

    public char[] getPassword() {
        return this.getPayPalOnlineService().getPassword();
    }

    public void setSignature(final String signature) {
        this.getPayPalOnlineService().setSignature(signature);
    }

    public String getSignature() {
        return this.getPayPalOnlineService().getSignature();
    }

    public void setAccountId(final int accountId) {
        this.getPayPalOnlineService().setAccountId(accountId);
    }

    public int getAccountId() {
        return this.getPayPalOnlineService().getAccountId();
    }
}
