// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import urn.ebay.api.PayPalAPI.GetBalanceReq;
import urn.ebay.api.PayPalAPI.GetBalanceRequestType;
import urn.ebay.api.PayPalAPI.GetBalanceResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * This service checks the balances of a PayPal account in order to determine
 * the available currencies.
 *
 * @author Florian J. Breunig
 */
public final class CheckCurrencyService
implements Callable<ServiceResult<CurrencyCodeType>> {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            CheckCurrencyService.class.getName());

    private static final AckCodeType[] ACK_CODES =
        {AckCodeType.SUCCESS, AckCodeType.SUCCESSWITHWARNING};

    private final Localizable localizable;
    private final PayPalAPIInterfaceServiceService service;
    private final Locale errorLocale;

    CheckCurrencyService(
            final PayPalAPIInterfaceServiceService argService,
            final Locale argErrorLocale,
            final Localizable argLocalizable) {
        this.localizable = argLocalizable;
        this.service = argService;
        this.errorLocale = argErrorLocale;
    }

    @Override
    public ServiceResult<CurrencyCodeType> call() {
        List<CurrencyCodeType> results = null;
        String errorCode = null;
        String errorMessage = null;

        GetBalanceRequestType reqType = new GetBalanceRequestType();
        reqType.setReturnAllCurrencies("1");
        reqType.setErrorLanguage(this.errorLocale.toString());

        GetBalanceReq req = new GetBalanceReq();
        req.setGetBalanceRequest(reqType);

        try {
            GetBalanceResponseType txnResponse = this.service.getBalance(req);

            if (txnResponse.getErrors() != null
                    && !txnResponse.getErrors().isEmpty()) {
                errorCode = txnResponse.getErrors().get(0).getErrorCode();
                errorMessage = txnResponse.getErrors().get(0).getLongMessage();
            }

            if (Arrays.asList(ACK_CODES).contains(txnResponse.getAck())) {
                txnResponse.getBalanceHoldings()
                        .forEach(amount -> LOG.info(String.format("Available currency: %s", amount.getCurrencyID())));

                results = txnResponse.getBalanceHoldings()
                        .stream()
                        .map(BasicAmountType::getCurrencyID)
                        .collect(Collectors.toList());
            }

        } catch (UnknownHostException | SocketException e) {
            logErrorMessage(e);
            errorMessage = this.localizable.getErrorMessageConnectionFailed();
        } catch (InterruptedException e) {
            logErrorMessage(e);
            errorMessage = e.getLocalizedMessage();
            Thread.currentThread().interrupt();
        } catch (IOException | SSLConfigurationException | InvalidCredentialException | HttpErrorException
                | InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
                | SAXException | OAuthException | ParserConfigurationException e) {
            logErrorMessage(e);
            errorMessage = e.getLocalizedMessage();
        }

        return new ServiceResult<>(results, errorCode, errorMessage);
    }

    private static void logErrorMessage(final Exception exception) {
        final String message = exception.getMessage();
        if (message != null) {
            LOG.log(Level.WARNING, message, exception);
        }
    }
}
