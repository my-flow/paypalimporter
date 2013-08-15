// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.AccountUtil;
import com.moneydance.apps.md.model.OnlineService;
import com.moneydance.apps.md.model.RootAccount;

import java.util.Iterator;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineService {

    private static final String KEY_SIGNATURE = String.format("signature_%s",
            OnlineService.DEFAULT_REQ_REALM);
    private static final String KEY_ACCOUNT = String.format("acct_%s",
            OnlineService.DEFAULT_REQ_REALM);
    private static final int INITIAL_NON_ZERO_ODD_NUMBER = 17;
    private static final int MULTIPLIER_NON_ZERO_ODD_NUMBER = 37;

    private final OnlineService onlineService;
    private final String authKey;

    PayPalOnlineService(final OnlineService argOnlineService) {
        Validate.notNull(argOnlineService, "online service must not be null");
        this.onlineService = argOnlineService;
        this.authKey = String.format("%s--%s--",
                this.onlineService.getFIOrg(), this.onlineService.getFIId());
    }

    public void assignToAccount(
            final RootAccount rootAccount,
            final int accountId) {
        Validate.notNull(rootAccount, "root account must not be null");

        final Account account = rootAccount.getAccountById(accountId);
        Validate.notNull(account, "account must not be null");

        if (account.getBankingFI() != null
                && account.getBankingFI().isSameAs(
                        this.onlineService.getServiceId())) {
            return;
        }

        Account nextAccount;
        for (Iterator<Account> iterator =
                AccountUtil.getAccountIterator(rootAccount);
                iterator.hasNext();) {
            nextAccount = iterator.next();
            if (nextAccount.getBankingFI() != null
                    && nextAccount.getBankingFI().isSameAs(
                            this.onlineService.getServiceId())) {
                nextAccount.setBankingFI(null);
            }
        }
        if (account.getBankingFI() == null) {
            // never override a preexisting OFX setting
            account.setBankingFI(this.onlineService);
        }
    }

    public void setUsername(final String username) {
        this.onlineService.setUserId(
                OnlineService.DEFAULT_REQ_REALM,
                null,
                username);
    }

    public String getUsername() {
        return this.onlineService.getUserId(
                OnlineService.DEFAULT_REQ_REALM,
                null);
    }

    public void setPassword(
            final RootAccount rootAccount,
            final char[] password) {

        if (rootAccount.getBooleanParameter(
                RootAccount.STORE_PINS_PARAM,
                false)) {
            this.onlineService.cacheAuthentication(
                    this.authKey,
                    String.valueOf(password));
        }
    }

    public char[] getPassword() {
        Object authObj = this.onlineService.getCachedAuthentication(
                this.authKey);
        char[] result;
        if (authObj == null) {
            result = null;
        } else {
            result = ((String) authObj).toCharArray();
        }
        return result;
    }

    public void setSignature(final String signature) {
        this.onlineService.getTable().put(KEY_SIGNATURE, signature);
    }

    public String getSignature() {
        return this.onlineService.getTable().getStr(KEY_SIGNATURE, null);
    }

    public void setAccountId(final int accountId) {
        this.onlineService.getTable().put(KEY_ACCOUNT, accountId);
    }

    public int getAccountId() {
        return this.onlineService.getTable().getInt(KEY_ACCOUNT, -1);
    }

    public void setUsedImportCombination(
            final String username,
            final int accountId) {
        this.onlineService.getTable().put(
                buildHashCode(username, accountId), true);
    }

    public boolean hasUsedImportCombination(
            final String username,
            final int accountId) {
        return this.onlineService.getTable().getBoolean(
                buildHashCode(username, accountId), false);
    }

    private static String buildHashCode(
            final String username,
            final int accountId) {
        return String.format("%d", new HashCodeBuilder(
                INITIAL_NON_ZERO_ODD_NUMBER, MULTIPLIER_NON_ZERO_ODD_NUMBER)
        .append(username)
        .append(accountId)
        .toHashCode());
    }
}
