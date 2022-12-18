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

import javax.annotation.Nullable;
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

@SuppressWarnings("PMD.GodClass")
public final class ServiceMock extends PayPalAPIInterfaceServiceService {

    @Nullable private List<BasicAmountType> balanceHoldings;
    @Nullable private AckCodeType ack;
    @Nullable private List<ErrorType> errors;

    @Nullable private UnknownHostException unknownHostException;
    @Nullable private SocketException socketException;
    @Nullable private IOException ioException;
    @Nullable private SSLConfigurationException sslConfigurationException;
    @Nullable private InvalidCredentialException invalidCredentialException;
    @Nullable private HttpErrorException httpErrorException;
    @Nullable private InvalidResponseDataException invalidResponseDataException;
    @Nullable private ClientActionRequiredException clientActionRequiredException;
    @Nullable private MissingCredentialException missingCredentialException;
    @Nullable private OAuthException oAuthException;
    @Nullable private InterruptedException interruptedException;
    @Nullable private ParserConfigurationException parserConfigurationException;
    @Nullable private SAXException saxException;
    @Nullable private List<PaymentTransactionSearchResultType> paymentTransactions;

    public ServiceMock(final InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    @SuppressWarnings("nullness")
    public GetBalanceResponseType getBalance(final GetBalanceReq getBalanceReq)
            throws SSLConfigurationException, InvalidCredentialException,
            IOException, HttpErrorException, InvalidResponseDataException,
            ClientActionRequiredException, MissingCredentialException,
            InterruptedException, OAuthException, ParserConfigurationException,
            SAXException  {

        if (this.unknownHostException != null) {
            throw this.unknownHostException;
        }
        if (this.socketException != null) {
            throw this.socketException;
        }
        if (this.ioException != null) {
            throw this.ioException;
        }
        if (this.sslConfigurationException != null) {
            throw this.sslConfigurationException;
        }
        if (this.invalidCredentialException != null) {
            throw this.invalidCredentialException;
        }
        if (this.httpErrorException != null) {
            throw this.httpErrorException;
        }
        if (this.invalidResponseDataException != null) {
            throw this.invalidResponseDataException;
        }
        if (this.clientActionRequiredException != null) {
            throw this.clientActionRequiredException;
        }
        if (this.missingCredentialException != null) {
            throw this.missingCredentialException;
        }
        if (this.oAuthException != null) {
            throw this.oAuthException;
        }
        if (this.interruptedException != null) {
            throw this.interruptedException;
        }
        if (this.parserConfigurationException != null) {
            throw this.parserConfigurationException;
        }
        if (this.saxException != null) {
            throw this.saxException;
        }

        GetBalanceResponseType response = new GetBalanceResponseType();
        response.setBalanceHoldings(this.balanceHoldings);
        response.setAck(this.ack);
        response.setErrors(this.errors);
        return response;
    }

    @Override
    @SuppressWarnings("nullness")
    public TransactionSearchResponseType transactionSearch(final TransactionSearchReq transactionSearchReq)
            throws SSLConfigurationException, InvalidCredentialException,
            IOException, HttpErrorException, InvalidResponseDataException,
            ClientActionRequiredException, MissingCredentialException,
            InterruptedException, OAuthException,
            ParserConfigurationException, SAXException  {

        if (this.unknownHostException != null) {
            throw this.unknownHostException;
        }
        if (this.socketException != null) {
            throw this.socketException;
        }
        if (this.ioException != null) {
            throw this.ioException;
        }
        if (this.sslConfigurationException != null) {
            throw this.sslConfigurationException;
        }
        if (this.invalidCredentialException != null) {
            throw this.invalidCredentialException;
        }
        if (this.httpErrorException != null) {
            throw this.httpErrorException;
        }
        if (this.invalidResponseDataException != null) {
            throw this.invalidResponseDataException;
        }
        if (this.clientActionRequiredException != null) {
            throw this.clientActionRequiredException;
        }
        if (this.missingCredentialException != null) {
            throw this.missingCredentialException;
        }
        if (this.oAuthException != null) {
            throw this.oAuthException;
        }
        if (this.interruptedException != null) {
            throw this.interruptedException;
        }
        if (this.parserConfigurationException != null) {
            throw this.parserConfigurationException;
        }
        if (this.saxException != null) {
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
