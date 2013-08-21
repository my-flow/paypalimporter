// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.AccountUtil;
import com.moneydance.apps.md.model.OnlineService;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineService {

    private static final Logger LOG = Logger
            .getLogger(PayPalOnlineService.class.getName());

    private final OnlineService onlineService;

    PayPalOnlineService(final OnlineService argOnlineService) {
        Validate.notNull(argOnlineService, "online service must not be null");
        this.onlineService = argOnlineService;
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

    public void setUsername(final int accountId, final String username) {
        this.onlineService.setUserId(
                buildRealm(accountId),
                null,
                username);
        this.onlineService.setMsgSetSignonRealm(
                accountId,
                buildRealm(accountId));
    }

    public String getUsername(final int accountId) {
        String username = this.onlineService.getUserId(
                buildRealm(accountId),
                null);
        if (StringUtils.isBlank(username) && this.getFirstRealm() != null) {
            username = this.onlineService.getUserId(this.getFirstRealm(), null);
        }
        return username;
    }

    public void setPassword(final int accountId, final char[] password) {
        this.onlineService.cacheAuthentication(
                buildAuthKey(buildRealm(accountId)),
                String.valueOf(password));
    }

    public char[] getPassword(final int accountId) {
        Object authObj = this.onlineService.getCachedAuthentication(
                buildAuthKey(buildRealm(accountId)));
        char[] result;
        if (authObj == null && this.getFirstRealm() != null) {
            authObj = this.onlineService.getCachedAuthentication(
                    buildAuthKey(this.getFirstRealm()));
        }
        if (authObj == null) {
            result = null;
        } else {
            result = ((String) authObj).toCharArray();
        }
        return result;
    }

    public void setSignature(final int accountId, final String signature) {
        this.onlineService.getTable().put(
                buildSignatureKey(buildRealm(accountId)),
                signature);
    }

    public String getSignature(final int accountId) {
        String signature = this.onlineService.getTable().getStr(
                buildSignatureKey(buildRealm(accountId)),
                null);
        if (signature == null && this.getFirstRealm() != null) {
            signature = this.onlineService.getTable().getStr(
                    buildSignatureKey(this.getFirstRealm()), null);
        }
        return signature;
    }

    private String getFirstRealm() {
        final String[] realms = this.onlineService.getRealms();
        if (realms != null && realms.length > 0) {
            LOG.config(String.format("realms[0]: %s", realms[0]));
            return realms[0];
        }
        return null;
    }

    private static String buildRealm(final int accountId) {
        if (accountId >= 0) {
            return String.format("realm_%d", accountId);
        }
        return OnlineService.DEFAULT_REQ_REALM;
    }

    private static String buildAuthKey(final String realm) {
        return String.format("%s:%s",
                Helper.INSTANCE.getSettings().getFIId(), realm);
    }

    private static String buildSignatureKey(final String realm) {
        return String.format("so_signature_%s", realm);
    }
}
