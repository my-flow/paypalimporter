package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountHelper;
import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.util.StreamTable;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;

public final class StubContextFactory {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(StubContextFactory.class
            .getName());

    @Nullable private final FeatureModule featureModule;
    private final StubContext context;

    public StubContextFactory() {
        this.featureModule = null;
        this.context = initContext(null, null);
    }

    public StubContextFactory(final FeatureModule argFeatureModule) {
        Validate.notNull(argFeatureModule, "feature module must not be null");
        this.featureModule = argFeatureModule;
        this.context = initContext(argFeatureModule, null);
    }

    private static StubContext initContext(
            @Nullable final FeatureModule argFeatureModule,
            @Nullable final OnlineInfo onlineInfo) {
       AccountBook accountBook = AccountBook.fakeAccountBook();
       try {
           accountBook.doInitialLoad(true);
           accountBook.initializeNewEmptyAccounts("USD");
        } catch (Exception e) {
           final String message = e.getMessage();
           if (message != null) {
               LOG.log(Level.SEVERE, message, e);
           }
       }
       accountBook.setLocalStorage(new StubLocalStorage(accountBook));

       StubAccountBook acctBook;
       if (onlineInfo == null) {
           acctBook = new StubAccountBook(accountBook);
       } else {
           acctBook = new StubAccountBook(accountBook, onlineInfo);
       }

       try {
           final Account acc1 = Account.makeAccount(
               accountBook,
               Account.AccountType.BANK,
               accountBook.getRootAccount()
           );
           acctBook.addAccount(acc1);
           AccountHelper.addSubAccount(
                   accountBook.getRootAccount(),
                   acc1
           );
       } catch (Exception e) {
           final String message = e.getMessage();
           if (message != null) {
               LOG.log(Level.SEVERE, message, e);
           }
       }

       return new StubContext(argFeatureModule, acctBook);
    }

    @SuppressWarnings("nullness")
    public void init() {
        LOG.info("Setting up stub context");
        this.featureModule.setup(
                this.context,
                "paypalimporter",
                new StreamTable(),
                null,
                null,
                false);
    }

    public StubContext getContext() {
        return this.context;
    }
}
