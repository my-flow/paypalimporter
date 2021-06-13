// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountListener;
import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.MoneydanceSyncableItem;
import com.infinitekind.moneydance.model.OnlineInfo;

/**
 * @author Florian J. Breunig
 */
public interface IAccountBook {

    OnlineInfo getOnlineInfo();

    Account getAccountById(String accountId);

    AccountBook getWrappedOriginal();

    CurrencyTable getCurrencies();

    Account getRootAccount();

    boolean logRemovedItem(MoneydanceSyncableItem removedItem);

    void addAccountListener(AccountListener accountListener);

    void removeAccountListener(AccountListener accountListener);

}
