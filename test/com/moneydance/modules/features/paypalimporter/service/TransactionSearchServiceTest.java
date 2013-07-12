/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
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

import com.moneydance.apps.md.controller.DateRange;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.controller.Util;
import com.moneydance.apps.md.model.time.DateRangeOption;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

/**
 * @author Florian J. Breunig
 */
public final class TransactionSearchServiceTest {

    private ServiceMock service;

    @Before
    public void setUp() {
        new StubContextFactory();
        try {
            this.service = new ServiceMock(
                    Helper.INSTANCE.getInputStreamFromResource(
                            "com/moneydance/modules/features/paypalimporter/resources/sdk_config.properties"));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void testCallSuccessfulEmpty() {
        this.service.setAck(AckCodeType.SUCCESS);
        this.service.setPaymentTransactions(Collections.<PaymentTransactionSearchResultType>emptyList());

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        PaymentTransactionSearchResultType transaction = new PaymentTransactionSearchResultType();
        this.service.setPaymentTransactions(Collections.singletonList(transaction));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        new DateRange(),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setUnknownHostException(new UnknownHostException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        false,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSocketException(new SocketException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setIOException(new IOException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        false,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSSLConfigurationException(new SSLConfigurationException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidCredentialException(new InvalidCredentialException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setHttpErrorException(new HttpErrorException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidResponseDataException(new InvalidResponseDataException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setClientActionRequiredException(new ClientActionRequiredException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setMissingCredentialException(new MissingCredentialException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setOAuthException(new OAuthException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInterruptedException(new InterruptedException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setParserConfigurationException(new ParserConfigurationException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSAXException(new SAXException("stub message"));

        Callable<ServiceResult<PaymentTransactionSearchResultType>> callable =
                new TransactionSearchService(
                        this.service,
                        CurrencyCodeType.USD,
                        true,
                        Util.calculateDateRange(
                                DateRangeOption.DR_LAST_12_MONTHS),
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
