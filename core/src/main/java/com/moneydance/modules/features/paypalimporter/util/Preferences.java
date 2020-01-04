// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.modules.features.paypalimporter.integration.OnlineServiceFactory;
import com.moneydance.modules.features.paypalimporter.integration.PayPalOnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    private final IAccountBook accountBook;
    private final UserPreferences userPreferences;
    private final OnlineServiceFactory onlineServiceFactory;
    @Nullable private PayPalOnlineService profile;

    Preferences(
            final FeatureModuleContext argContext,
            final IAccountBook argAccountBook,
            final Settings argSettings) {
        this.userPreferences = ((Main) argContext).getPreferences();
        this.accountBook = argAccountBook;
        this.onlineServiceFactory = new OnlineServiceFactory(argSettings);
    }

    private PayPalOnlineService getPayPalOnlineService() {
        if (this.profile == null) {
            this.profile = this.onlineServiceFactory.createService(this.accountBook);
        }
        return this.profile;
    }

    @SuppressWarnings("nullness")
    public void setAllWritablePreferencesToNull() {
        this.userPreferences.setSetting(KEY_FIRST_RUN, (String) null);
        this.onlineServiceFactory.removeService(this.accountBook);
    }

    public void setFirstRun(final boolean firstRun) {
        this.userPreferences.setSetting(KEY_FIRST_RUN, firstRun);
    }

    public boolean isFirstRun() {
        return this.userPreferences.getBoolSetting(KEY_FIRST_RUN, true);
    }

    public Locale getLocale() {
        return this.userPreferences.getLocale();
    }

    public boolean hasProxy() {
        return this.userPreferences.getBoolSetting(
                UserPreferences.NET_USE_PROXY,
                false);
    }

    public String getProxyHost() {
        return this.userPreferences.getSetting(
                UserPreferences.NET_PROXY_HOST);
    }

    public int getProxyPort() {
        return this.userPreferences.getIntSetting(
                UserPreferences.NET_PROXY_PORT,
                0);
    }

    public boolean hasProxyAuthentication() {
        return this.userPreferences.getBoolSetting(
                UserPreferences.NET_AUTH_PROXY,
                false);
    }

    public String getProxyUsername() {
        return this.userPreferences.getSetting(
                UserPreferences.NET_PROXY_USER);
    }

    public String getProxyPassword() {
        return this.userPreferences.getSetting(
                UserPreferences.NET_PROXY_PASS);
    }

    public void assignBankingFI(final String accountId) {
        this.getPayPalOnlineService().assignToAccount(
                this.accountBook, accountId);
    }

    public void setUsername(final String accountId, @Nullable final String username) {
        this.getPayPalOnlineService().setUsername(accountId, username);
    }

    public String getUsername(final String accountId) {
        return this.getPayPalOnlineService().getUsername(accountId);
    }

    @SuppressWarnings("nullness")
    public void setPassword(final String accountId, @Nonnull final char[] password) {
        this.getPayPalOnlineService().setPassword(accountId, password);
    }

    @SuppressWarnings("nullness")
    public char[] getPassword(final String accountId) {
        return this.getPayPalOnlineService().getPassword(accountId);
    }

    public void setSignature(final String accountId, @Nullable final String signature) {
        this.getPayPalOnlineService().setSignature(accountId, signature);
    }

    public String getSignature(final String accountId) {
        return this.getPayPalOnlineService().getSignature(accountId);
    }

    public void setAccountId(final String accountId) {
        this.getPayPalOnlineService().setAccountId(accountId);
    }

    public String getAccountId() {
        return this.getPayPalOnlineService().getAccountId();
    }

    public boolean hasUsedCombination(
            final String accountId,
            final String username) {

        return username != null && username.equals(
                this.getPayPalOnlineService().getUsername(accountId));
    }
}
