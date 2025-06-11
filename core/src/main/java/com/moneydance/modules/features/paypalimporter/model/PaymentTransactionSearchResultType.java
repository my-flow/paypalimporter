package com.moneydance.modules.features.paypalimporter.model;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Immutable representation of a PayPal payment transaction search result.
 * This class replaces the usage of PayPal SDK's PaymentTransactionSearchResultType.
 */
public final class PaymentTransactionSearchResultType {

    private final String payer;
    private final String payerDisplayName;
    private final String timestamp;
    private final String transactionID;
    private final String status;
    private final String type;
    private final BasicAmount grossAmount;

    /**
     * Constructs a new PaymentTransactionSearchResultType with the specified values.
     *
     * @param argPayer the payer information
     * @param argPayerDisplayName the payer display name
     * @param argTimestamp the transaction timestamp
     * @param argTransactionID the transaction ID
     * @param argStatus the transaction status
     * @param argType the transaction type
     * @param argGrossAmount the gross amount of the transaction
     */
    public PaymentTransactionSearchResultType(
            @Nullable final String argPayer,
            @Nullable final String argPayerDisplayName,
            @Nullable final String argTimestamp,
            @Nullable final String argTransactionID,
            @Nullable final String argStatus,
            @Nullable final String argType,
            @Nullable final BasicAmount argGrossAmount) {
        this.payer = argPayer;
        this.payerDisplayName = argPayerDisplayName;
        this.timestamp = argTimestamp;
        this.transactionID = argTransactionID;
        this.status = argStatus;
        this.type = argType;
        this.grossAmount = argGrossAmount;
    }

    /**
     * Gets the payer information.
     *
     * @return the payer, may be null
     */
    @Nullable
    public String getPayer() {
        return this.payer;
    }

    /**
     * Gets the payer display name.
     *
     * @return the payer display name, may be null
     */
    @Nullable
    public String getPayerDisplayName() {
        return this.payerDisplayName;
    }

    /**
     * Gets the transaction timestamp.
     *
     * @return the timestamp, may be null
     */
    @Nullable
    public String getTimestamp() {
        return this.timestamp;
    }

    /**
     * Gets the transaction ID.
     *
     * @return the transaction ID, may be null
     */
    @Nullable
    public String getTransactionID() {
        return this.transactionID;
    }

    /**
     * Gets the transaction status.
     *
     * @return the status, may be null
     */
    @Nullable
    public String getStatus() {
        return this.status;
    }

    /**
     * Gets the transaction type.
     *
     * @return the type, may be null
     */
    @Nullable
    public String getType() {
        return this.type;
    }

    /**
     * Gets the gross amount of the transaction.
     *
     * @return the gross amount, may be null
     */
    @Nullable
    public BasicAmount getGrossAmount() {
        return this.grossAmount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PaymentTransactionSearchResultType that = (PaymentTransactionSearchResultType) obj;
        return Objects.equals(payer, that.payer)
                && Objects.equals(payerDisplayName, that.payerDisplayName)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(transactionID, that.transactionID)
                && Objects.equals(status, that.status)
                && Objects.equals(type, that.type)
                && Objects.equals(grossAmount, that.grossAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payer, payerDisplayName, timestamp, transactionID,
                status, type, grossAmount);
    }

    @Override
    public String toString() {
        return "PaymentTransactionSearchResultType{"
                + "payer='" + payer + '\''
                + ", payerDisplayName='" + payerDisplayName + '\''
                + ", timestamp='" + timestamp + '\''
                + ", transactionID='" + transactionID + '\''
                + ", status='" + status + '\''
                + ", type='" + type + '\''
                + ", grossAmount='" + grossAmount + '\''
                + '}';
    }
}
