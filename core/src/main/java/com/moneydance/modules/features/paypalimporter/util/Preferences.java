// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.modules.features.paypalimporter.integration.OnlineServiceFactory;
import com.moneydance.modules.features.paypalimporter.integration.PayPalOnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;

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

    private final IAccountBookFactory accountBookFactory;
    private final UserPreferences userPreferences;
    private final FeatureModuleContext context;
    private final OnlineServiceFactory onlineServiceFactory;
    @Nullable private IAccountBook accountBook;
    @Nullable private PayPalOnlineService profile;

    Preferences(
            final FeatureModuleContext argContext,
            final IAccountBookFactory argAccountBookFactory,
            final Settings argSettings) {
        this.userPreferences = ((Main) argContext).getPreferences();
        this.accountBookFactory = argAccountBookFactory;
        this.context = argContext;
        this.onlineServiceFactory = new OnlineServiceFactory(argSettings);
    }

    private IAccountBook getAccountBook() {
        if (this.accountBook == null) {
            this.accountBook = this.accountBookFactory.createAccountBook(context).orElseThrow(AssertionError::new);
        }
        return this.accountBook;
    }

    private PayPalOnlineService getPayPalOnlineService() {
        if (this.profile == null) {
            this.profile = this.onlineServiceFactory.createService(this.getAccountBook());
        }
        return this.profile;
    }

    @SuppressWarnings("nullness")
    public void setAllWritablePreferencesToNull() {
        this.userPreferences.setSetting(KEY_FIRST_RUN, (String) null);
        this.onlineServiceFactory.removeService(this.getAccountBook());
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

    public void assignBankingFI(final int accountId) {
        this.getPayPalOnlineService().assignToAccount(
                this.getAccountBook(), accountId);
    }

    public void setUsername(final int accountId, @Nullable final String username) {
        this.getPayPalOnlineService().setUsername(accountId, username);
    }

    public String getUsername(final int accountId) {
        return this.getPayPalOnlineService().getUsername(accountId);
    }

    @SuppressWarnings("nullness")
    public void setPassword(final int accountId, @Nonnull final char[] password) {
        this.getPayPalOnlineService().setPassword(accountId, password);
    }

    @SuppressWarnings("nullness")
    public char[] getPassword(final int accountId) {
        return this.getPayPalOnlineService().getPassword(accountId);
    }

    public void setSignature(final int accountId, @Nullable final String signature) {
        this.getPayPalOnlineService().setSignature(accountId, signature);
    }

    public String getSignature(final int accountId) {
        return this.getPayPalOnlineService().getSignature(accountId);
    }

    public void setAccountId(final int accountId) {
        this.getPayPalOnlineService().setAccountId(accountId);
    }

    public int getAccountId() {
        return this.getPayPalOnlineService().getAccountId();
    }

    public boolean hasUsedCombination(
            final int accountId,
            final String username) {

        return username != null && username.equals(
                this.getPayPalOnlineService().getUsername(accountId));
    }
}
