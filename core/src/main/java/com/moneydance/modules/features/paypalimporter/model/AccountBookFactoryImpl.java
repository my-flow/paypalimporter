// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.AccountBook;
import com.moneydance.apps.md.controller.FeatureModuleContext;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Florian J. Breunig
 */
public enum AccountBookFactoryImpl implements IAccountBookFactory {

    INSTANCE;

    private final Map<AccountBook, AccountBookImpl> multitons =
            new Hashtable<AccountBook, AccountBookImpl>();

    @Override
    @Nullable public AccountBookImpl createAccountBook(
            final FeatureModuleContext context) {
      final AccountBook accountBook = context.getCurrentAccountBook();
      if (accountBook == null) {
          return null;
      }
      if (this.multitons.containsKey(accountBook)) {
          return this.multitons.get(accountBook);
      }
      this.multitons.clear();
      AccountBookImpl value = new AccountBookImpl(context);
      this.multitons.put(accountBook, value);
      return value;
    }
}
