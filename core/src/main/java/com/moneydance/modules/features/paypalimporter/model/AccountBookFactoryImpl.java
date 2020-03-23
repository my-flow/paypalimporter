// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.AccountBook;
import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Florian J. Breunig
 */
public enum AccountBookFactoryImpl implements IAccountBookFactory {

    INSTANCE;

    private final Map<AccountBook, AccountBookImpl> multitons = new ConcurrentHashMap<>();

    @Override
    public Optional<IAccountBook> createAccountBook(
            final FeatureModuleContext context) {
      final AccountBook accountBook = context.getCurrentAccountBook();
      if (accountBook == null) {
          return Optional.empty();
      }
      final AccountBookImpl impl = this.multitons.get(accountBook);
      if (impl != null) {
          return Optional.of(impl);
      }
      this.multitons.clear();
      AccountBookImpl value = new AccountBookImpl(context);
      this.multitons.put(accountBook, value);
      return Optional.of(value);
    }
}
