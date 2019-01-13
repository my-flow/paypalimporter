// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

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

    Account getAccountByNum(final int accountId);

    AccountBook getWrappedOriginal();

    CurrencyTable getCurrencies();

    Account getRootAccount();

    boolean logRemovedItem(final MoneydanceSyncableItem removedItem);

    void addAccountListener(final AccountListener accountListener);

    void removeAccountListener(final AccountListener accountListener);

}
