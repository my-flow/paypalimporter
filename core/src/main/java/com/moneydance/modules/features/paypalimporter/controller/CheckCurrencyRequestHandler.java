package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.moneydance.modules.features.paypalimporter.domain.CurrencyMapperUtil;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import java.util.List;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * Callback class that handles currency checks.
 */
final class CheckCurrencyRequestHandler
extends AbstractRequestHandler<CurrencyCodeType> {

    private final IAccountBook accountBook;
    private final String accountId;

    CheckCurrencyRequestHandler(
            final ViewController argViewController,
            final IAccountBook argAccountBook,
            final String argAccountId,
            final Localizable argLocalizable) {
        super(argViewController,
                argLocalizable);
        this.accountBook = argAccountBook;
        this.accountId = argAccountId;
    }


    @Override
    public void serviceCallSucceeded(
            final ServiceResult<CurrencyCodeType> serviceResult) {

        final List<CurrencyCodeType> currencyCodes = serviceResult.getResults().orElseThrow(AssertionError::new);
        Account useAccount = this.accountBook.getAccountById(this.accountId);

        // 1. determine the currency of the Moneydance account
        CurrencyType currencyType;
        if (useAccount == null) {
            // no Moneydance account was found, so create a new Moneydance
            // account later with the first currency of the PayPal account.
            currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                    currencyCodes.get(0),
                    this.accountBook.getCurrencies(),
                    this.accountBook);
        } else {
            // a Moneydance account was found, so take its currency
            currencyType = useAccount.getCurrencyType();
        }

        // 2. determine the currency of the PayPal account
        CurrencyCodeType currencyCode =
                CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                        currencyType,
                        currencyCodes);
        this.getViewController().currencyChecked(
                currencyType,
                currencyCode,
                currencyCodes);
    }
}
