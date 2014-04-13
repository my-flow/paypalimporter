// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
import com.moneydance.modules.features.paypalimporter.util.Helper;
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
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.Validate;
import org.xml.sax.SAXException;

import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.TransactionSearchReq;
import urn.ebay.api.PayPalAPI.TransactionSearchRequestType;
import urn.ebay.api.PayPalAPI.TransactionSearchResponseType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionClassCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
public final class TransactionSearchService
implements Callable<ServiceResult<PaymentTransactionSearchResultType>> {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            TransactionSearchService.class.getName());

    private static final PaymentTransactionClassCodeType TXN_CLASS =
            PaymentTransactionClassCodeType.BALANCEAFFECTING;

    private static final AckCodeType[] ACK_CODES =
        {AckCodeType.SUCCESS, AckCodeType.SUCCESSWITHWARNING};

    private final Localizable localizable;
    private final PayPalAPIInterfaceServiceService service;
    private final CurrencyCodeType currencyCode;
    private final Date startDate;
    private final Date endDate;
    private final Locale errorLocale;
    private final DateFormat dateFormat;

    public TransactionSearchService(
            final PayPalAPIInterfaceServiceService argService,
            final CurrencyCodeType argCurrencyCode,
            final Date argStartDate,
            final Date argEndDate,
            final Locale argErrorLocale) {
        this.localizable = Helper.INSTANCE.getLocalizable();
        Validate.notNull(argService, "service must not be null");
        Validate.notNull(argCurrencyCode, "currency code must not be null");
        Validate.notNull(argStartDate, "start date must not be null");
        Validate.notNull(argEndDate, "end date must not be null");
        Validate.notNull(argErrorLocale, "error locale must not be null");
        this.service = argService;
        this.currencyCode = argCurrencyCode;
        this.startDate = argStartDate;
        this.endDate = argEndDate;
        this.errorLocale = argErrorLocale;
        this.dateFormat = Helper.INSTANCE.getSettings().getDateFormat();
    }

    @Override
    public ServiceResult<PaymentTransactionSearchResultType> call() {

        List<PaymentTransactionSearchResultType> results = null;
        String errorCode = null;
        String errorMessage = null;

        TransactionSearchRequestType txnType =
                new TransactionSearchRequestType();
        txnType.setCurrencyCode(this.currencyCode);
        txnType.setTransactionClass(TXN_CLASS);

        final Date sDate = DateConverter.getValidDate(this.startDate);
        txnType.setStartDate(this.dateFormat.format(sDate));
        final Date eDate = DateConverter.getValidDate(this.endDate);
        txnType.setEndDate(this.dateFormat.format(eDate));

        txnType.setErrorLanguage(this.errorLocale.toString());

        TransactionSearchReq txnreq = new TransactionSearchReq();
        txnreq.setTransactionSearchRequest(txnType);

        try {
            TransactionSearchResponseType txnResponse =
                    this.service.transactionSearch(txnreq);

            if (txnResponse.getErrors() != null
                    && !txnResponse.getErrors().isEmpty()) {
                errorCode = txnResponse.getErrors().get(0).getErrorCode();
                errorMessage = txnResponse.getErrors().get(0).getLongMessage();
            }

            if (Arrays.asList(ACK_CODES).contains(txnResponse.getAck())) {
                results = txnResponse.getPaymentTransactions();
            }

        } catch (UnknownHostException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = this.localizable.getErrorMessageConnectionFailed();
        } catch (SocketException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = this.localizable.getErrorMessageConnectionFailed();
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (SSLConfigurationException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (InvalidCredentialException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (HttpErrorException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (InvalidResponseDataException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (ClientActionRequiredException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (MissingCredentialException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (OAuthException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (InterruptedException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (ParserConfigurationException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        } catch (SAXException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            errorMessage = e.getLocalizedMessage();
        }

        return new ServiceResult<PaymentTransactionSearchResultType>(
                results,
                errorCode,
                errorMessage);
    }
}
