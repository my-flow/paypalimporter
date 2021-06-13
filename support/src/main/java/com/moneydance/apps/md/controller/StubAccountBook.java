// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
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
        this.onlineInfo = argOnlineInfo;
    }

    public void addAccount(final Account account) {
        this.accountsById.put(account.getUUID(), account);
    }

    public void removeAccount(final Account account) {
        this.accountsById.remove(account.getUUID());
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
