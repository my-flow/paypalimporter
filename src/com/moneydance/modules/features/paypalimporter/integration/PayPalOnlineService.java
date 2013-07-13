/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.integration;

import java.util.Iterator;

import org.apache.commons.lang3.Validate;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.AccountUtil;
import com.moneydance.apps.md.model.OnlineService;
import com.moneydance.apps.md.model.RootAccount;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineService {

    private static final String KEY_SIGNATURE = String.format("signature_%s",
            OnlineService.DEFAULT_REQ_REALM);
    private static final String KEY_ACCOUNT = String.format("acct_%s",
            OnlineService.DEFAULT_REQ_REALM);

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

        for (Iterator<Account> iterator =
                AccountUtil.getAccountIterator(rootAccount);
                iterator.hasNext();) {
            final Account nextAccount = iterator.next();
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
        if (authObj == null) {
            return null;
        }
        return ((String) authObj).toCharArray();
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
}
