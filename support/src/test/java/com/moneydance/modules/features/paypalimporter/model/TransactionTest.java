package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.OnlineTxnList;
import com.infinitekind.util.StreamTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class TransactionTest {

    @Test
    public void testEquals() {
        final AccountBook accountBook = AccountBook.fakeAccountBook();
        final OnlineTxnList txnList = new OnlineTxnList(accountBook, new StreamTable());

        final Transaction t1 = new Transaction(txnList, "t1", 1, "1", "1", 1);
        assertEquals(t1, t1);

        final Transaction t2 = t1;
        assertEquals(t1, t2);

        final Transaction t3 = new Transaction(txnList, "t1", 2, "2", "2", 2);
        assertEquals(t1, t3);

        final Transaction t4 = new Transaction(txnList, "t4", 1, "1", "1", 1);
        assertNotEquals(t1, t4);

        assertNotEquals(t1, "string");
    }

    @Test
    public void testHashCode() {
        final AccountBook accountBook = AccountBook.fakeAccountBook();
        final OnlineTxnList txnList = new OnlineTxnList(accountBook, new StreamTable());
        final Transaction t1 = new Transaction(txnList, "t1", 1, "1", "1", 1);
        final Transaction t2 = new Transaction(txnList, "t1", 2, "2", "2", 2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
