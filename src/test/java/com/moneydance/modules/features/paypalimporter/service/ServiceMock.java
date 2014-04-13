// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import urn.ebay.api.PayPalAPI.GetBalanceReq;
import urn.ebay.api.PayPalAPI.GetBalanceResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.TransactionSearchReq;
import urn.ebay.api.PayPalAPI.TransactionSearchResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
public final class ServiceMock extends PayPalAPIInterfaceServiceService {

    private List<BasicAmountType> balanceHoldings;
    private AckCodeType ack;
    private List<ErrorType> errors;

    private UnknownHostException unknownHostException;
    private SocketException socketException;
    private IOException ioException;
    private SSLConfigurationException sslConfigurationException;
    private InvalidCredentialException invalidCredentialException;
    private HttpErrorException httpErrorException;
    private InvalidResponseDataException invalidResponseDataException;
    private ClientActionRequiredException clientActionRequiredException;
    private MissingCredentialException missingCredentialException;
    private OAuthException oAuthException;
    private InterruptedException interruptedException;
    private ParserConfigurationException parserConfigurationException;
    private SAXException saxException;
    private List<PaymentTransactionSearchResultType> paymentTransactions;

    public ServiceMock(final InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    public GetBalanceResponseType getBalance(final GetBalanceReq getBalanceReq)
            throws SSLConfigurationException, InvalidCredentialException,
            IOException, HttpErrorException, InvalidResponseDataException,
            ClientActionRequiredException, MissingCredentialException,
            InterruptedException, OAuthException, ParserConfigurationException,
            SAXException  {

        if (this.unknownHostException != null) {
            throw this.unknownHostException;
        } else if (this.socketException != null) {
            throw this.socketException;
        } else if (this.ioException != null) {
            throw this.ioException;
        } else if (this.sslConfigurationException != null) {
            throw this.sslConfigurationException;
        } else if (this.invalidCredentialException != null) {
            throw this.invalidCredentialException;
        } else if (this.httpErrorException != null) {
            throw this.httpErrorException;
        } else if (this.invalidResponseDataException != null) {
            throw this.invalidResponseDataException;
        } else if (this.clientActionRequiredException != null) {
            throw this.clientActionRequiredException;
        } else if (this.missingCredentialException != null) {
            throw this.missingCredentialException;
        } else if (this.oAuthException != null) {
            throw this.oAuthException;
        } else if (this.interruptedException != null) {
            throw this.interruptedException;
        } else if (this.parserConfigurationException != null) {
            throw this.parserConfigurationException;
        } else if (this.saxException != null) {
            throw this.saxException;
        }

        GetBalanceResponseType response = new GetBalanceResponseType();
        response.setBalanceHoldings(this.balanceHoldings);
        response.setAck(this.ack);
        response.setErrors(this.errors);
        return response;
    }

    @Override
    public TransactionSearchResponseType transactionSearch(final TransactionSearchReq transactionSearchReq)
            throws SSLConfigurationException, InvalidCredentialException,
            IOException, HttpErrorException, InvalidResponseDataException,
            ClientActionRequiredException, MissingCredentialException,
            InterruptedException, OAuthException,
            ParserConfigurationException, SAXException  {

        if (this.unknownHostException != null) {
            throw this.unknownHostException;
        } else if (this.socketException != null) {
            throw this.socketException;
        } else if (this.ioException != null) {
            throw this.ioException;
        } else if (this.sslConfigurationException != null) {
            throw this.sslConfigurationException;
        } else if (this.invalidCredentialException != null) {
            throw this.invalidCredentialException;
        } else if (this.httpErrorException != null) {
            throw this.httpErrorException;
        } else if (this.invalidResponseDataException != null) {
            throw this.invalidResponseDataException;
        } else if (this.clientActionRequiredException != null) {
            throw this.clientActionRequiredException;
        } else if (this.missingCredentialException != null) {
            throw this.missingCredentialException;
        } else if (this.oAuthException != null) {
            throw this.oAuthException;
        } else if (this.interruptedException != null) {
            throw this.interruptedException;
        } else if (this.parserConfigurationException != null) {
            throw this.parserConfigurationException;
        } else if (this.saxException != null) {
            throw this.saxException;
        }

        TransactionSearchResponseType response = new TransactionSearchResponseType();
        response.setPaymentTransactions(this.paymentTransactions);
        response.setAck(this.ack);
        response.setErrors(this.errors);
        return response;
    }

    public void setBalanceHoldings(
            final List<BasicAmountType> argBalanceHoldings) {
        this.balanceHoldings = argBalanceHoldings;
    }

    public void setPaymentTransactions(
            final List<PaymentTransactionSearchResultType> argPaymentTransactions) {
        this.paymentTransactions = argPaymentTransactions;
    }

    public void setAck(final AckCodeType argAck) {
        this.ack = argAck;
    }

    public void setErrors(final List<ErrorType> argErrors) {
        this.errors = argErrors;
    }

    public void setUnknownHostException(
            final UnknownHostException argUnknownHostException) {
        this.unknownHostException = argUnknownHostException;
    }

    public void setSocketException(
            final SocketException argSocketException) {
        this.socketException = argSocketException;
    }

    public void setIOException(
            final IOException argIoException) {
        this.ioException = argIoException;
    }

    public void setSSLConfigurationException(
            final SSLConfigurationException argSslConfigurationException) {
        this.sslConfigurationException = argSslConfigurationException;
    }

    public void setInvalidCredentialException(
            final InvalidCredentialException argInvalidCredentialException) {
        this.invalidCredentialException = argInvalidCredentialException;
    }

    public void setHttpErrorException(
            final HttpErrorException argHttpErrorException) {
        this.httpErrorException = argHttpErrorException;
    }

    public void setInvalidResponseDataException(
            final InvalidResponseDataException argInvalidResponseDataException) {
        this.invalidResponseDataException = argInvalidResponseDataException;
    }

    public void setClientActionRequiredException(
            final ClientActionRequiredException argClientActionRequiredException) {
        this.clientActionRequiredException = argClientActionRequiredException;
    }

    public void setMissingCredentialException(
            final MissingCredentialException argMissingCredentialException) {
        this.missingCredentialException = argMissingCredentialException;
    }

    public void setOAuthException(final OAuthException argOAuthException) {
        this.oAuthException = argOAuthException;
    }

    public void setInterruptedException(
            final InterruptedException argInterruptedException) {
        this.interruptedException = argInterruptedException;
    }

    public void setParserConfigurationException(
            final ParserConfigurationException argParserConfigurationException) {
        this.parserConfigurationException = argParserConfigurationException;
    }

    public void setSAXException(final SAXException argSaxException) {
        this.saxException = argSaxException;
    }
}
