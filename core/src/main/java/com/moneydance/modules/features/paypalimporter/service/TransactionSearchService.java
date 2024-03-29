package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
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
 * This service fetches all transactions between two given dates.
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
    private final DateConverter dateConverter;
    private final PayPalAPIInterfaceServiceService service;
    private final CurrencyCodeType currencyCode;
    private final Date startDate;
    private final Date endDate;
    private final Locale errorLocale;
    private final DateFormat dateFormat;

    TransactionSearchService(
            final PayPalAPIInterfaceServiceService argService,
            final Localizable argLocalizable,
            final DateConverter argDateConverter,
            final CurrencyCodeType argCurrencyCode,
            final Date argStartDate,
            final Date argEndDate,
            final Locale argErrorLocale,
            final DateFormat argDateFormat) {
        this.localizable = argLocalizable;
        this.dateConverter = argDateConverter;
        this.service = argService;
        this.currencyCode = argCurrencyCode;
        this.startDate = new Date(argStartDate.getTime());
        this.endDate = new Date(argEndDate.getTime());
        this.errorLocale = argErrorLocale;
        this.dateFormat = argDateFormat;
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

        final Date sDate = this.dateConverter.getValidDate(this.startDate);
        txnType.setStartDate(this.dateFormat.format(sDate));
        final Date eDate = this.dateConverter.getValidDate(this.endDate);
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

        } catch (UnknownHostException | SocketException e) {
            logErrorMessage(e);
            errorMessage = this.localizable.getErrorMessageConnectionFailed();
        } catch (InterruptedException e) {
            logErrorMessage(e);
            errorMessage = e.getLocalizedMessage();
            Thread.currentThread().interrupt();
        } catch (IOException | SSLConfigurationException | HttpErrorException | InvalidCredentialException
                | InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
                | OAuthException | ParserConfigurationException | SAXException e) {
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
