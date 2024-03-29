package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.MoneydanceSyncableItem;
import com.infinitekind.moneydance.model.OnlineInfo;

public interface IAccountBook {

    String KEY_ACCOUNT_URL = "account_url";

    OnlineInfo getOnlineInfo();

    Account getAccountById(String accountId);

    AccountBook getWrappedOriginal();

    CurrencyTable getCurrencies();

    Account getRootAccount();

    Account createBankAccount(
            String newAccountName,
            CurrencyType argCurrencyType,
            String accountURL);

    boolean logRemovedItem(MoneydanceSyncableItem removedItem);

    void addAccountListener(AccountListener accountListener);

    void removeAccountListener(AccountListener accountListener);

}
