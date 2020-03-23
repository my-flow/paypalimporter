// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.
package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.Account;
import com.moneydance.apps.md.view.gui.AccountListModel;

import javax.swing.ComboBoxModel;

/**
 * @author Florian J. Breunig
 */
public final class AccountFilter {

    private final AccountListModel accountListModel;

    AccountFilter(final IAccountBook argAccountBook) {
        this.accountListModel = new AccountListModel(
                argAccountBook.getRootAccount());
        this.accountListModel.setShowBankAccounts(true);
        this.accountListModel.setShowCreditCardAccounts(true);
        this.accountListModel.setShowInvestAccounts(true);
        this.accountListModel.setShowAssetAccounts(true);
        this.accountListModel.setShowLiabilityAccounts(true);
        this.accountListModel.setShowLoanAccounts(true);
    }

    public ComboBoxModel getComboBoxModel() {
        return this.accountListModel;
    }

    public void setSelectedAccount(final Account account) {
        if (this.isValidAccount(account)) {
            this.accountListModel.setSelectedAccount(account);
        }
    }

    boolean isValidAccount(final Account account) {
        return account != null && this.accountListModel.isAccountViewable(account);
    }
}
