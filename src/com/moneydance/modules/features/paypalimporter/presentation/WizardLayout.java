/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.presentation;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.Validate;
import org.jdesktop.xswingx.PromptSupport;

import com.jgoodies.common.internal.ResourceBundleLocalizer;
import com.jgoodies.common.internal.StringLocalizer;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.moneydance.apps.md.view.gui.DateRangeChooser;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.util.Helper;

/**
 * @author Florian J. Breunig
 */
class WizardLayout extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final RowSpec GAP_ROW = RowSpec.createGap(Sizes.DLUX9);

    final PanelBuilder panelBuilder;
    final DateRangeChooser dateRanger;
    final JTextField txtUsername;
    final AbstractButton btnHelp;
    final JPasswordField txtPassword;
    final JTextField txtSignature;
    final AbstractButton rdBtnExistingAcct;
    final JComboBox comboBoxAccts;
    final AbstractButton rdBtnNewAcct;
    final JComponent comboBoxDateRange;
    final JButton btnProceed;
    final JButton btnCancel;
    final JProgressBar progressBar;

    /**
     * Create the frame.
     * @param owner
     * @param resourceBundle the resource bundle used to lookup i15d strings
     */
    WizardLayout(
            final Frame owner,
            final MoneydanceGUI mdGUI,
            final ResourceBundle resourceBundle) {
        super(owner, false);
        Validate.notNull(mdGUI, "Moneydance GUI must not be null");
        this.dateRanger = new DateRangeChooser(mdGUI);

        Validate.notNull(resourceBundle, "resource bundle must not be null");
        StringLocalizer localizer = new ResourceBundleLocalizer(resourceBundle);

        this.setTitle(localizer.getString("title_wizard"));

        this.txtUsername = new JTextField();
        this.btnHelp = new HelpButton();
        this.txtPassword = new JPasswordField();
        this.txtSignature = new JTextField();
        this.rdBtnExistingAcct = new JRadioButton();
        this.comboBoxAccts = new JComboBox();
        this.rdBtnNewAcct = new JRadioButton();
        this.comboBoxDateRange = this.dateRanger.getChoice();
        this.progressBar = new JProgressBar();
        this.btnCancel = new JButton();
        this.btnProceed = new JButton();

        // add rows dynamically
        final FormLayout layout = new FormLayout(
                Helper.INSTANCE.getSettings().getColumnSpecs());

        DefaultFormBuilder builder = new DefaultFormBuilder(
                layout, localizer);
        builder.border(Borders.DIALOG);

        PromptSupport.setPrompt(
                localizer.getString("hint_username"),
                this.txtUsername);
        this.txtUsername.setToolTipText(null);
        builder.appendI15d("label_username", this.txtUsername,  9);
        builder.append(this.btnHelp);

        PromptSupport.setPrompt(
                localizer.getString("hint_password"),
                this.txtPassword);
        this.txtPassword.setToolTipText(null);
        builder.appendI15d("label_password", this.txtPassword, 11);

        PromptSupport.setPrompt(
                localizer.getString("hint_signature"),
                this.txtSignature);
        this.txtSignature.setToolTipText(null);
        builder.appendI15d("label_signature", this.txtSignature, 11);

        builder.appendRow(GAP_ROW);
        builder.nextLine(2);

        builder.appendI15d(
                String.format("%s%s",
                        mdGUI.getStr("import_into_acct"),
                        mdGUI.getStr("labelColon")),
                        this.rdBtnExistingAcct, 3);
        builder.append(this.comboBoxAccts, 7);
        MnemonicUtils.configure(
                this.rdBtnExistingAcct,
                String.format(
                        "%s%s",
                        mdGUI.getStr("existing_account"),
                        mdGUI.getStr("labelColon")));

        builder.leadingColumnOffset(2);
        builder.append(this.rdBtnNewAcct, 11);
        MnemonicUtils.configure(
                this.rdBtnNewAcct,
                mdGUI.getStr("new_account"));

        builder.appendRow(GAP_ROW);
        builder.nextLine(2);

        builder.leadingColumnOffset(0);
        builder.append(this.dateRanger.getChoiceLabel());
        builder.append(this.comboBoxDateRange, 11);

        builder.leadingColumnOffset(2);
        builder.append(this.dateRanger.getStartField(), 3);
        builder.appendI15d("label_custom_date_to",
                this.dateRanger.getEndField(), 5);

        builder.appendRow(GAP_ROW);
        builder.nextLine(2);

        builder.appendGlueRow();
        builder.nextLine(1);

        builder.leadingColumnOffset(0);
        this.progressBar.setPreferredSize(
                new Dimension(
                        2 * Sizes.DLUX21.getPixelSize(this),
                        this.progressBar.getPreferredSize().height));
        builder.append(this.progressBar);
        builder.nextColumn(5);
        builder.add(this.btnCancel,
                CC.xyw(
                        builder.getColumn(),
                        builder.getRow(),
                        2,
                        CellConstraints.RIGHT,
                        CellConstraints.DEFAULT));
        MnemonicUtils.configure(
                this.btnCancel,
                mdGUI.getStr("cancel"));
        builder.nextColumn(3);
        builder.append(this.btnProceed, 3);
        MnemonicUtils.configure(
                this.btnProceed,
                mdGUI.getStr("import"));
        this.panelBuilder = builder;

        this.setContentPane(this.panelBuilder.getPanel());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.pack();
    }
}
