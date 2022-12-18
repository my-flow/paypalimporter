package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.OnlineTxn;
import com.infinitekind.moneydance.model.OnlineTxnList;
import com.infinitekind.tiksync.SyncRecord;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public final class Transaction extends OnlineTxn {

    private final OnlineTxnList txnList;

    public Transaction(
            final OnlineTxnList argTxnList,
            final String argTransactionId,
            final long argAmount,
            final String argDescription,
            final String argMemo,
            final long argDate) {

        super(argTxnList, new SyncRecord());
        this.txnList = argTxnList;
        this.setProtocolType(OnlineTxn.PROTO_TYPE_OFX);
        this.setAmount(argAmount);
        this.setTotalAmount(argAmount);
        this.setName(argDescription);
        this.setMemo(argMemo);
        this.setFITxnId(argTransactionId);
        this.setDatePosted(argDate);
        this.setDateInitiated(argDate);
        this.setDateAvailable(argDate);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31) // two randomly chosen prime numbers
                .append(this.getFITxnId())
                .append(this.txnList)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Transaction)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        final Transaction rhs = (Transaction) obj;
        return new EqualsBuilder()
                .appendSuper(super.isSameAs(rhs))
                .isEquals();
    }
}
