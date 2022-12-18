package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.MoneydanceSyncableItem;
import com.infinitekind.moneydance.model.OnlineInfo;
import com.moneydance.apps.md.controller.FeatureModuleContext;

import javax.annotation.Nullable;

final class AccountBookImpl implements IAccountBook {

    private final AccountBook accountBook;

    AccountBookImpl(final FeatureModuleContext context) {
        this.accountBook = context.getCurrentAccountBook();
    }

    @Override
    public OnlineInfo getOnlineInfo() {
        return this.accountBook.getOnlineInfo();
    }

    @Override
    @Nullable public Account getAccountById(final String accountId) {
        return this.accountBook.getAccountByUUID(accountId);
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
        this.accountBook.registerNewItemWithoutSyncing(account);
        this.accountBook.logModifiedItem(account);
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
