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

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;

import com.moneydance.apps.md.controller.StubContextFactory;
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
public final class CheckCurrencyServiceTest {

    private ServiceMock service;

    @Before
    public void setUp() throws IOException {
        new StubContextFactory();
        this.service = new ServiceMock(
                Helper.INSTANCE.getInputStreamFromResource(
                        "com/moneydance/modules/features/paypalimporter/resources/sdk_config.properties"));
    }

    @Test
    public void testCallSuccessfulEmpty() throws Exception {
        this.service.setAck(AckCodeType.SUCCESS);
        this.service.setBalanceHoldings(Collections.<BasicAmountType>emptyList());

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), nullValue());
    }

    @Test
    public void testCallSuccessfulFilled() throws Exception {
        this.service.setAck(AckCodeType.SUCCESSWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        BasicAmountType amountType = new BasicAmountType(CurrencyCodeType.USD, "1.00");
        this.service.setBalanceHoldings(Collections.singletonList(amountType));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFail() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailUnknownHostException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setUnknownHostException(new UnknownHostException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailSocketException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSocketException(new SocketException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailIOException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setIOException(new IOException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailSSLException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSSLConfigurationException(new SSLConfigurationException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailInvalidCredentialException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidCredentialException(new InvalidCredentialException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailHttpErrorException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setHttpErrorException(new HttpErrorException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailInvalidResponseDataException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInvalidResponseDataException(new InvalidResponseDataException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailClientActionRequiredException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setClientActionRequiredException(new ClientActionRequiredException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailMissingCredentialException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setMissingCredentialException(new MissingCredentialException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailOAuthException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setOAuthException(new OAuthException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailInterruptedException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setInterruptedException(new InterruptedException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailParserConfigurationException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setParserConfigurationException(new ParserConfigurationException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }

    @Test
    public void testCallFailSAXException() throws Exception {
        this.service.setAck(AckCodeType.FAILUREWITHWARNING);
        ErrorType errorType = new ErrorType();
        errorType.setErrorCode("mock error code");
        errorType.setLongMessage("long error message");
        this.service.setErrors(Collections.singletonList(errorType));
        this.service.setSAXException(new SAXException("stub message"));

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.service,
                        Locale.US);
        ServiceResult<CurrencyCodeType> serviceResult = callable.call();
        assertThat(serviceResult, notNullValue());
        assertThat(serviceResult.getErrorMessage(), notNullValue());
    }
}
