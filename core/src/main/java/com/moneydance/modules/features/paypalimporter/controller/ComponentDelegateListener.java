package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.AccountListener;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Logger;

/**
 * Listener / observer on the main window.
 */
final class ComponentDelegateListener extends ComponentAdapter {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            ComponentDelegateListener.class.getName());

    private final IAccountBook accountBook;
    private final AccountListener accountListener;

    ComponentDelegateListener(
            final IAccountBook argAccountBook,
            final ViewController argViewController) {
        super();
        this.accountBook = argAccountBook;
        this.accountListener = new AccountDelegateListener(argViewController);
    }

    @Override
    public void componentShown(final ComponentEvent event) {
        LOG.config("Show wizard");
        this.accountBook.addAccountListener(this.accountListener);
    }

    @Override
    public void componentHidden(final ComponentEvent event) {
        LOG.config("Hide wizard");
        this.accountBook.removeAccountListener(this.accountListener);
    }
}
