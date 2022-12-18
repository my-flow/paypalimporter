package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.MoneydanceSyncableItem;
import com.infinitekind.moneydance.model.OnlineInfo;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Florian J. Breunig
 */
public final class StubAccountBook implements IAccountBook {

    private final AccountBook accountBook;
    private final Map<String, Account> accountsById;
    @Nullable private final OnlineInfo onlineInfo;

    public StubAccountBook(final AccountBook argAccountBook) {
        this(argAccountBook, null);
    }

    public StubAccountBook(final AccountBook argAccountBook,
            @Nullable final OnlineInfo argOnlineInfo) {
        this.accountBook = argAccountBook;
        this.accountsById = new Hashtable<>();
        if (argAccountBook != null) {
            addAccount(argAccountBook.getRootAccount());
        }
        this.onlineInfo = argOnlineInfo;
    }

    public void addAccount(final Account account) {
        if (account != null) {
            this.accountsById.put(account.getUUID(), account);
        }
    }

    public void removeAccount(final Account account) {
        if (account != null) {
            this.accountsById.remove(account.getUUID());
        }
    }

    @Override
    public OnlineInfo getOnlineInfo() {
        if (this.onlineInfo == null) {
            return this.accountBook.getOnlineInfo();
        }
        return this.onlineInfo;
    }

    @Override
    @SuppressWarnings("nullness")
    public Account getAccountById(final String accountId) {
        return this.accountsById.get(accountId);
    }

    @Override
    public AccountBook getWrappedOriginal() {
        return this.accountBook;
    }

    @Override
    public CurrencyTable getCurrencies() {
        return this.accountBook.getCurrencies();
    }

    @Override
    public Account getRootAccount() {
        return this.accountBook.getRootAccount();
    }

    @Override
    public Account createBankAccount(
            final String newAccountName,
            final CurrencyType argCurrencyType,
            final String accountURL) {
        Account account = Account.makeAccount(
                this.accountBook,
                Account.AccountType.BANK,
                this.getRootAccount());
        account.setAccountName(newAccountName);
        account.setCurrencyType(argCurrencyType);
        account.setParameter(KEY_ACCOUNT_URL, accountURL);
        addAccount(account);
        return account;
    }

    @Override
    public boolean logRemovedItem(final MoneydanceSyncableItem removedItem) {
        return this.accountBook.logRemovedItem(removedItem);
    }

    @Override
    public void addAccountListener(final AccountListener accountListener) {
        this.accountBook.addAccountListener(accountListener);
    }

    @Override
    public void removeAccountListener(final AccountListener accountListener) {
        this.accountBook.removeAccountListener(accountListener);
    }
}
