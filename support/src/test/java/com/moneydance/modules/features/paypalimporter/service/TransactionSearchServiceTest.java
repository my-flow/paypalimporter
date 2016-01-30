// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.util.Helper;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
public final class TransactionSearchServiceTest {

    private static final String ERROR_CODE = "mock error code";
    private static final String LONG_MESSAGE = "long error message";
    private static final String STUB_MESSAGE = "stub message";

    private ServiceMock service;
    private Date startDate;
    private Date endDate;

    @Before
    public void setUp() {
        new StubContextFactory();
        try {
            this.service = new ServiceMock(
                    Helper.getInputStreamFromResource(
                            "sdk_config.properties"));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.DATE, -1);
        this.startDate = startCal.getTime();

        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.DATE, -1);
        this.endDate = endCal.getTime();
    }


    @Test
    public void testCallSuccessfulEmpty() {
        this.service.setAck(AckCodeType.SUCCESS);
        this.service.setPaymentTransactions(Collections.<PaymentTransactionSearchResultType>emptyList());

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), nullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallSuccessfulFilled() {
        this.service.setAck(AckCodeType.SUCCESSWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        PaymentTransactionSearchResultType transaction = new PaymentTransactionSearchResultType();
        this.service.setPaymentTransactions(Collections.singletonList(transaction));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFail() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailUnknownHostException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setUnknownHostException(new UnknownHostException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailSocketException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSocketException(new SocketException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailIOException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setIOException(new IOException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailSSLException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSSLConfigurationException(new SSLConfigurationException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailInvalidCredentialException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidCredentialException(new InvalidCredentialException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailHttpErrorException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setHttpErrorException(new HttpErrorException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailInvalidResponseDataException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidResponseDataException(new InvalidResponseDataException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailClientActionRequiredException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setClientActionRequiredException(new ClientActionRequiredException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailMissingCredentialException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setMissingCredentialException(new MissingCredentialException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailOAuthException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setOAuthException(new OAuthException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailInterruptedException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInterruptedException(new InterruptedException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailParserConfigurationException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setParserConfigurationException(new ParserConfigurationException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallFailSAXException() {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode(ERROR_CODE);
        errorType.setLongMessage(LONG_MESSAGE);
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSAXException(new SAXException(STUB_MESSAGE));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        this.startDate,
                        this.endDate,
                        Locale.US);
        try {
            ServiceResult<PaymentTransactionSearchResultType> serviceResult = callable.call();
            assertThat(serviceResult, notNullValue());
            assertThat(serviceResult.getErrorMessage(), notNullValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
