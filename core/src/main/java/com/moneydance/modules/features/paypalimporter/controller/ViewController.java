// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.OnlineTxn;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Observer;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import javax.annotation.Nullable;

import com.moneydance.modules.features.paypalimporter.model.InputData;
/**
 * Controller interface, strategy pattern.
 *
 * @author Florian J. Breunig
 */
public interface ViewController extends Observer {

    /**
     * Start the import process.
     */
    void startWizard();

    /**
     * Cancel a running import process.
     */
    void cancel();

    /**
     * Continue to the next step of the import process.
     * @param inputData user input to proceed with
     */
    void proceed(InputData inputData);

    /**
     * Unlock the input fields of the view.
     */
    void unlock();

    /**
     * Unlock the input fields of the view.
     *
     * @param text error message to be displayed
     * @param key identifier of the related input field
     */
    void unlock(String text, Object key);

    /**
     * This hook is called after a service call has checked the available
     * currencies.
     *
     * @param currencyType Moneydance's currency of the account (at this point
     * the Moneydance account can already exist or not).
     * @param currencyCode Matching currency of the PayPal account.
     * @param currencyCodes All currencies of the PayPal account. Might be
     * required to display this information to the user later.
     */
    void currencyChecked(
            CurrencyType currencyType,
            CurrencyCodeType currencyCode,
            List<CurrencyCodeType> currencyCodes);

    /**
     * This hook is called after a service call has fetched a batch of or all
     * transactions.
     *
     * @param onlineTxns All fetched transactions.
     * @param argStartDate Earliest date of the fetched transactions.
     * @param account Moneydance account that will import the transactions.
     * @param errorCode Error from PayPal that might come with the transactions.
     */
    void transactionsImported(
            List<OnlineTxn> onlineTxns,
            Date argStartDate,
            @Nullable Account account,
            @Nullable String errorCode);

    /**
     * Show instructions to the user how to find the credentials.
     */
    void showHelp() throws MalformedURLException;

    /**
     * Notify the view to refresh the list of accounts.
     *
     * @param selectedAccountId Preselected account in the combobox.
     */
    void refreshAccounts(int selectedAccountId);
}
