// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyTable;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineInfo;
import com.moneydance.apps.md.model.OnlineService;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.util.StreamTable;

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

    private static final String KEY_SERVICE_TYPE = "type";

    private final FeatureModule featureModule;
    private final StubContext context;

    public StubContextFactory() {
        this.featureModule = null;
        this.context = new StubContext(this.featureModule);
        this.initContext();
    }

    public StubContextFactory(final FeatureModule argFeatureModule) {
        Validate.notNull(argFeatureModule, "featureModule must not be null");
        this.featureModule = argFeatureModule;
        this.context = new StubContext(this.featureModule);
        this.initContext();
    }

    private void initContext() {
        CurrencyType currencyType = new CurrencyType(-1, "USD",
                "Test Currency", 1.0D, 0, "$", "", "USD",
                CurrencyType.CURRTYPE_CURRENCY, 0, null);

        RootAccount rootAccount = new RootAccount(currencyType,
                new CurrencyTable());
        try {
            rootAccount.addSubAccount(Account.makeAccount(
                    Account.ACCOUNT_TYPE_BANK, "stub account 1", currencyType,
                    rootAccount));
            rootAccount.addSubAccount(Account.makeAccount(
                    Account.ACCOUNT_TYPE_BANK, "stub account 2", currencyType,
                    rootAccount));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.context.setRootAccount(rootAccount);
        Helper.INSTANCE.setContext(this.context);
    }

    public void init() {
        LOG.info("Setting up stub context");
        this.featureModule.setup(this.context, null, new StreamTable(), null,
                null);
    }

    public StubContext getContext() {
        return this.context;
    }

    public StubContextFactory addOnlineService() {
        final StreamTable table = new StreamTable(1);
        OnlineInfo onlineInfo = this.context.getRootAccount().getOnlineInfo();
        table.put(KEY_SERVICE_TYPE, Settings.getServiceType());
        OnlineService onlineService = new OnlineService(
                onlineInfo,
                table);
        onlineInfo.addService(onlineService);
        return this;
    }
}
