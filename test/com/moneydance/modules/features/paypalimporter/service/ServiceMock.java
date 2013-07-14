// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

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

    /**
     * Setter for balanceHoldings
     */
    public void setBalanceHoldings(
            final List<BasicAmountType> balanceHoldings) {
        this.balanceHoldings = balanceHoldings;
    }

    /**
     * Setter for paymentTransactions
     */
    public void setPaymentTransactions(
            final List<PaymentTransactionSearchResultType> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    /**
     * Setter for ack
     */
    public void setAck(final AckCodeType ack) {
        this.ack = ack;
    }

    /**
     * Setter for errors
     */
    public void setErrors(final List<ErrorType> errors) {
        this.errors = errors;
    }

    /**
     * Setter for Exception
     */
    public void setUnknownHostException(
            final UnknownHostException unknownHostException) {
        this.unknownHostException = unknownHostException;
    }

    /**
     * Setter for Exception
     */
    public void setSocketException(
            final SocketException socketException) {
        this.socketException = socketException;
    }

    /**
     * Setter for Exception
     */
    public void setIOException(
            final IOException ioException) {
        this.ioException = ioException;
    }

    /**
     * Setter for Exception
     */
    public void setSSLConfigurationException(
            final SSLConfigurationException sslConfigurationException) {
        this.sslConfigurationException = sslConfigurationException;
    }

    /**
     * Setter for Exception
     */
    public void setInvalidCredentialException(
            final InvalidCredentialException invalidCredentialException) {
        this.invalidCredentialException = invalidCredentialException;
    }

    /**
     * Setter for Exception
     */
    public void setHttpErrorException(
            final HttpErrorException httpErrorException) {
        this.httpErrorException = httpErrorException;
    }

    /**
     * Setter for Exception
     */
    public void setInvalidResponseDataException(
            final InvalidResponseDataException invalidResponseDataException) {
        this.invalidResponseDataException = invalidResponseDataException;
    }

    /**
     * Setter for Exception
     */
    public void setClientActionRequiredException(
            final ClientActionRequiredException clientActionRequiredException) {
        this.clientActionRequiredException = clientActionRequiredException;
    }

    /**
     * Setter for Exception
     */
    public void setMissingCredentialException(
            final MissingCredentialException missingCredentialException) {
        this.missingCredentialException = missingCredentialException;
    }

    /**
     * Setter for Exception
     */
    public void setOAuthException(final OAuthException oAuthException) {
        this.oAuthException = oAuthException;
    }

    /**
     * Setter for Exception
     */
    public void setInterruptedException(
            final InterruptedException interruptedException) {
        this.interruptedException = interruptedException;
    }

    /**
     * Setter for Exception
     */
    public void setParserConfigurationException(
            final ParserConfigurationException parserConfigurationException) {
        this.parserConfigurationException = parserConfigurationException;
    }

    /**
     * Setter for Exception
     */
    public void setSAXException(final SAXException saxException) {
        this.saxException = saxException;
    }
}
