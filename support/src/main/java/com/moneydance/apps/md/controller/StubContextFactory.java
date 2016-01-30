// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.AccountHelper;
import com.infinitekind.moneydance.model.OnlineInfo;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.util.StreamTable;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

/**
 * @author Florian J. Breunig
 */
public final class StubContextFactory {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(StubContextFactory.class
            .getName());

    private final FeatureModule featureModule;
    private final StubContext context;

    public StubContextFactory() {
        this.featureModule = null;
        this.context = initContext(null, null);
        Helper.INSTANCE.setContext(this.context);
    }

    public StubContextFactory(final OnlineInfo onlineInfo) {
        Validate.notNull(onlineInfo, "online info must not be null");
        this.featureModule = null;
        this.context = initContext(null, onlineInfo);
        Helper.INSTANCE.setContext(this.context);
    }

    public StubContextFactory(final FeatureModule argFeatureModule) {
        Validate.notNull(argFeatureModule, "feature module must not be null");
        this.featureModule = argFeatureModule;
        this.context = initContext(argFeatureModule, null);
        Helper.INSTANCE.setContext(this.context);
    }

    private static StubContext initContext(final FeatureModule argFeatureModule,
            final OnlineInfo onlineInfo) {
       AccountBook accountBook = AccountBook.fakeAccountBook();
       accountBook.initializeNewEmptyAccounts("USD");
       accountBook.setLocalStorage(new StubLocalStorage());

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
           AccountHelper.addSubAccount(
               accountBook.getRootAccount(),
               acc1
           );
           acctBook.addAccount(acc1);

           final Account acc2 = Account.makeAccount(
               accountBook,
               Account.AccountType.BANK,
               accountBook.getRootAccount()
           );
           AccountHelper.addSubAccount(
               accountBook.getRootAccount(),
               acc2
           );
           acctBook.addAccount(acc2);

       } catch (Exception e) {
           LOG.log(Level.SEVERE, e.getMessage(), e);
       }

       return new StubContext(argFeatureModule, acctBook);
    }

    public void init() {
        LOG.info("Setting up stub context");
        this.featureModule.setup(this.context, null, new StreamTable(), null,
                null);
    }

    public StubContext getContext() {
        return this.context;
    }
}
