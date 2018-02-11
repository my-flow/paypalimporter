// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.moneydance.modules.features.paypalimporter.domain.CurrencyMapper;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;

import java.util.List;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * Callback class that handles currency checks.
 *
 * @author Florian J. Breunig
 */
final class CheckCurrencyRequestHandler
extends AbstractRequestHandler<CurrencyCodeType> {

    private final IAccountBook accountBook;
    private final int accountNum;

    CheckCurrencyRequestHandler(
            final ViewController argViewController,
            final IAccountBook argAccountBook,
            final int argAccountNum) {
        super(argViewController);
        this.accountBook = argAccountBook;
        this.accountNum = argAccountNum;
    }


    @Override
    public void serviceCallSucceeded(
            final ServiceResult<CurrencyCodeType> serviceResult) {

        List<CurrencyCodeType> currencyCodes = serviceResult.getResults();
        assert currencyCodes != null : "@AssumeAssertion(nullness)";

        Account useAccount = this.accountBook.getAccountByNum(this.accountNum);

        // 1. determine the currency of the Moneydance account
        CurrencyType currencyType;
        if (useAccount == null) {
            // no Moneydance account was found, so create a new Moneydance
            // account later with the first currency of the PayPal account.
            currencyType = CurrencyMapper.getCurrencyTypeFromCurrencyCode(
                    currencyCodes.get(0),
                    this.accountBook.getCurrencies(),
                    this.accountBook);
        } else {
            // a Moneydance account was found, so take its currency
            currencyType = useAccount.getCurrencyType();
        }

        // 2. determine the currency of the PayPal account
        CurrencyCodeType currencyCode =
                CurrencyMapper.getCurrencyCodeFromCurrencyType(
                        currencyType,
                        currencyCodes);
        this.getViewController().currencyChecked(
                currencyType,
                currencyCode,
                currencyCodes);
    }
}