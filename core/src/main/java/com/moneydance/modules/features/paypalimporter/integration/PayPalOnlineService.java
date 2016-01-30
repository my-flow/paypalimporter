// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountUtil;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Decorator class for an <code>OnlineService</code> that saves user name,
 * password, and signature.
 *
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

    /**
     * Assign this <code>PayPalOnlineService</code> to a Moneydance
     * <code>Account</code>. That means the credentials of a PayPal account
     * will be associated with a Moneydance account.
     *
     * @param accountBook Global account book
     * @param accountId Identifier of the account
     */
    public void assignToAccount(
            final IAccountBook accountBook,
            final int accountId) {
        Validate.notNull(accountBook, "account book must not be null");

        final Account account = accountBook.getAccountByNum(accountId);
        Validate.notNull(account, "account must not be null");

        if (this.onlineService.isSameAs(account.getBankingFI())) {
            return;
        }

        Account nextAccount;
        for (Iterator<Account> iterator = AccountUtil.getAccountIterator(
                accountBook.getWrappedOriginal());
            iterator.hasNext();) {
            nextAccount = iterator.next();
            if (this.onlineService.isSameAs(nextAccount.getBankingFI())) {
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
        this.onlineService.addParameters(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                this.put(buildSignatureKey(buildRealm(accountId)), signature);
            }
        });
    }

    public String getSignature(final int accountId) {
        String signature = this.onlineService.getParameter(
                buildSignatureKey(buildRealm(accountId)),
                null);
        if (signature == null && this.getFirstRealm() != null) {
            signature = this.onlineService.getParameter(
                    buildSignatureKey(this.getFirstRealm()), null);
        }
        return signature;
    }

    private String getFirstRealm() {
        final List<String> realms = this.onlineService.getRealms();
        if (realms != null && !realms.isEmpty()) {
            LOG.config(String.format("realms[0]: %s", realms.get(0)));
            return realms.get(0);
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
