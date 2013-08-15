// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.domain.CurrencyMapper;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;

import java.util.List;

import org.apache.commons.lang3.Validate;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
final class CheckCurrencyRequestHandler
extends AbstractRequestHandler<CurrencyCodeType> {

    private final RootAccount rootAccount;
    private final int accountNum;

    CheckCurrencyRequestHandler(
            final ViewController argViewController,
            final RootAccount argRootAccount,
            final int argAccountNum) {
        super(argViewController);

        Validate.notNull(argRootAccount, "root account must not be null");
        this.rootAccount = argRootAccount;
        this.accountNum = argAccountNum;
    }


    @Override
    public void serviceCallSucceeded(
            final ServiceResult<CurrencyCodeType> serviceResult) {

        List<CurrencyCodeType> currencyCodes = serviceResult.getResults();
        Validate.notEmpty(currencyCodes, "currency codes must not be empty");

        Account useAccount = this.rootAccount.getAccountById(this.accountNum);
        CurrencyType currencyType;
        if (useAccount == null) {
            currencyType = CurrencyMapper.getCurrencyTypeFromCurrencyCode(
                    currencyCodes.get(0),
                    this.rootAccount.getCurrencyTable());
        } else {
            currencyType = useAccount.getCurrencyType();
        }

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
