// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.MoneydanceSyncableItem;
import com.infinitekind.moneydance.model.OnlineInfo;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Florian J. Breunig
 */
public final class StubAccountBook implements IAccountBook {

    private final AccountBook accountBook;
    private final Map<Integer, Account> accountsByNum;
    private final OnlineInfo onlineInfo;

    public StubAccountBook(final AccountBook argAccountBook) {
        this(argAccountBook, null);
    }

    public StubAccountBook(final AccountBook argAccountBook,
            final OnlineInfo argOnlineInfo) {
        this.accountBook   = argAccountBook;
        this.accountsByNum = new Hashtable<Integer, Account>();
        this.onlineInfo    = argOnlineInfo;
    }

    public void addAccount(final Account account) {
        this.accountsByNum.put(account.getAccountNum(), account);
    }

    @Override
    public OnlineInfo getOnlineInfo() {
        if (this.onlineInfo == null) {
            return this.accountBook.getOnlineInfo();
        }
        return this.onlineInfo;
    }

    @Override
    public Account getAccountByNum(final int accountId) {
        return this.accountsByNum.get(accountId);
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
