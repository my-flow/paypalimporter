package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountUtil;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Decorator class for an <code>OnlineService</code> that saves user name,
 * password, and signature.
 */
public final class PayPalOnlineService {

    private static final Logger LOG = Logger
            .getLogger(PayPalOnlineService.class.getName());

    private final OnlineService onlineService;
    private final String fiId;

    PayPalOnlineService(
            final OnlineService argOnlineService,
            final String argFIId) {
        this.onlineService = argOnlineService;
        this.fiId = argFIId;
    }

    /**
     * Assign this <code>PayPalOnlineService</code> to a Moneydance
     * <code>Account</code>. That means the credentials of a PayPal account
     * will be associated with a Moneydance account.
     *
     * @param accountBook Global account book
     * @param accountId Identifier of the account
     */
    @SuppressWarnings("nullness")
    public void assignToAccount(
            final IAccountBook accountBook,
            final String accountId) {
        final Account account = accountBook.getAccountById(accountId);

        if (this.onlineService.isSameAs(account.getBankingFI())) {
            return;
        }

        Iterable<Account> iterable = () -> AccountUtil.getAccountIterator(accountBook.getWrappedOriginal());
        StreamSupport
                .stream(iterable.spliterator(), false)
                .filter(nextAccount -> this.onlineService.isSameAs(nextAccount.getBankingFI()))
                .forEach(nextAccount -> nextAccount.setBankingFI(null));

        if (account.getBankingFI() == null) {
            // never override a preexisting OFX setting
            account.setBankingFI(this.onlineService);
        }
    }

    @SuppressWarnings("nullness")
    public void setUsername(final String accountId, @Nullable final String username) {
        this.onlineService.setUserId(
                buildRealm(accountId),
                null,
                username);
    }

    @SuppressWarnings("nullness")
    public String getUsername(final String accountId) {
        String username = this.onlineService.getUserId(
                buildRealm(accountId),
                null);
        final Optional<String> firstRealm = this.getFirstRealm();
        if (firstRealm.isPresent() && StringUtils.isBlank(username)) {
            username = this.onlineService.getUserId(firstRealm.get(), null);
        }
        return username;
    }

    @SuppressWarnings("nullness")
    public void setPassword(final String accountId, @Nonnull final char[] password) {
        this.onlineService.cacheAuthentication(
                buildAuthKey(buildRealm(accountId)),
                String.valueOf(password));
    }

    @SuppressWarnings("nullness")
    public char[] getPassword(final String accountId) {
        Object authObj = this.onlineService.getCachedAuthentication(
                buildAuthKey(buildRealm(accountId)));
        char[] result;
        final Optional<String> firstRealm = this.getFirstRealm();
        if (authObj == null && firstRealm.isPresent()) {
            authObj = this.onlineService.getCachedAuthentication(
                    buildAuthKey(firstRealm.get()));
        }
        if (authObj == null) {
            result = null;
        } else {
            result = ((String) authObj).toCharArray();
        }
        return result;
    }

    @SuppressWarnings("initialization")
    public void setSignature(final String accountId, @Nullable final String signature) {
        this.onlineService.setParameter(buildSignatureKey(buildRealm(accountId)), signature);
    }

    @SuppressWarnings("nullness")
    public String getSignature(final String accountId) {
        String signature = this.onlineService.getParameter(
                buildSignatureKey(buildRealm(accountId)),
                null);
        final Optional<String> firstRealm = this.getFirstRealm();
        if (signature == null && firstRealm.isPresent()) {
            signature = this.onlineService.getParameter(
                    buildSignatureKey(firstRealm.get()), null);
        }
        return signature;
    }

    public void setAccountId(final String accountId) {
        this.onlineService.setParameter(buildAccountKey(buildRealm(null)), accountId);
    }

    public String getAccountId() {
        String accountId = this.onlineService.getParameter(
                buildAccountKey(buildRealm(null)),
                null);
        final Optional<String> firstRealm = this.getFirstRealm();
        if (accountId == null && firstRealm.isPresent()) {
            accountId = this.onlineService.getParameter(
                    buildAccountKey(firstRealm.get()), null);
        }

        return accountId;
    }

    static String buildRealm(final String accountId) {
        if (accountId != null && (!NumberUtils.isNumber(accountId) || Integer.parseInt(accountId) >= 0)) {
            return String.format("realm_%s", accountId);
        }
        return OnlineService.DEFAULT_REQ_REALM;
    }

    private static String buildSignatureKey(final String realm) {
        return String.format("so_signature_%s", realm);
    }

    private static String buildAccountKey(final String realm) {
        return String.format("so_account_%s", realm);
    }

    private Optional<String> getFirstRealm() {
        final Optional<String> realm = this.onlineService.getRealms().stream().findFirst();
        LOG.config(String.format("realms[0]: %s", realm));
        return realm;
    }

    private String buildAuthKey(final String realm) {
        return String.format("%s:%s", this.fiId, realm);
    }
}
