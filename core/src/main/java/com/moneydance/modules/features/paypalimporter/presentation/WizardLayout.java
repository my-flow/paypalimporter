package com.moneydance.modules.features.paypalimporter.presentation;

import com.infinitekind.moneydance.model.Account;
import com.jgoodies.common.internal.ResourceBundleAccessor;
import com.jgoodies.common.internal.StringResourceAccessor;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.Sizes;
import com.moneydance.apps.md.view.gui.DateRangeChooser;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.annotation.Nullable;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * This view class creates the static layout based on JGoodies Forms.
 */
class WizardLayout extends JDialog {

    private static final long serialVersionUID = 1L;

    protected final JPanel jpanel;
    protected final JTextField txtUsername;
    protected final AbstractButton btnHelp;
    protected final JPasswordField txtPassword;
    protected final JTextField txtSignature;
    protected final AbstractButton rdBtnExistingAcct;
    protected final JComboBox<Account> comboBoxAccts;
    protected final AbstractButton rdBtnNewAcct;
    protected final JComponent comboBoxDateRange;
    protected final JButton btnProceed;
    protected final JButton btnCancel;
    protected final JProgressBar progressBar;
    protected transient DateRangeChooser dateRanger;

    /**
     * Create the frame.
     * @param owner the {@code Frame} from which the dialog is displayed
     * @param mdGUI {@code MoneydanceGUI} that can resolve i18n translations
     * @param resourceBundle the resource bundle used to lookup i15d strings
     */
    @SuppressWarnings("initialization")
    WizardLayout(
            @Nullable final Frame owner,
            final MoneydanceGUI mdGUI,
            final ResourceBundle resourceBundle,
            final Settings settings) {
        super(owner, false);
        this.dateRanger = new DateRangeChooser(mdGUI);

        StringResourceAccessor localizer = new ResourceBundleAccessor(
                resourceBundle);

        this.setTitle(localizer.getString("title_wizard"));

        this.txtUsername = new PTextField(localizer.getString("hint_username"));
        this.btnHelp = new HelpButton(settings.getHelpImage());
        this.txtPassword = new JPasswordField();
        this.txtPassword.setToolTipText(localizer.getString("hint_password"));
        this.txtSignature = new PTextField(localizer.getString("hint_signature"));
        this.rdBtnExistingAcct = new JRadioButton();
        this.comboBoxAccts = new JComboBox<>();
        this.rdBtnNewAcct = new JRadioButton();
        this.comboBoxDateRange = this.dateRanger.getChoice();
        this.progressBar = new JProgressBar();
        this.btnCancel = new JButton();
        this.btnProceed = new JButton();

        Color background = mdGUI.getColors().defaultBackground;
        this.rdBtnExistingAcct.setBackground(background);
        this.rdBtnNewAcct.setBackground(background);

        MnemonicUtils.configure(
            this.rdBtnExistingAcct,
            String.format(
                "%s%s",
                mdGUI.getStr("existing_account"),
                localizer.getString("label_colon")));
        MnemonicUtils.configure(
            this.rdBtnNewAcct,
            mdGUI.getStr("new_account"));
        MnemonicUtils.configure(
            this.btnCancel,
            mdGUI.getStr("cancel"));
        MnemonicUtils.configure(
            this.btnProceed,
            localizer.getString("label_import_button"));

        this.progressBar.setPreferredSize(new Dimension(
            2 * Sizes.DLUX21.getPixelSize(this, true),
            this.progressBar.getPreferredSize().height));


        this.jpanel = FormBuilder.create()
            .columns(settings.getColumnSpecs())
            .rows(settings.getRowsSpecs())
            .background(background)
            .padding(Paddings.DIALOG)
            .debug(false)

            .add(localizer.getString("label_username"))
                .labelFor(this.txtUsername).xy(1, 1)
            .add(this.txtUsername).xyw(3, 1, 9)
            .add(this.btnHelp).xy(13, 1)

            .add(localizer.getString("label_password"))
                .labelFor(this.txtPassword).xy(1, 3)
            .add(this.txtPassword).xyw(3, 3, 11)

            .add(localizer.getString("label_signature"))
                .labelFor(this.txtSignature).xy(1, 5)
            .add(this.txtSignature).xyw(3, 5, 11)

            .add(String.format("%s%s",
                mdGUI.getStr("import_into_acct"),
                localizer.getString("label_colon")))
                .labelFor(this.rdBtnExistingAcct).xy(1, 7)

            .add(this.rdBtnExistingAcct).xyw(3, 7, 3)
                .add(this.comboBoxAccts).xyw(7, 7, 7)
            .add(this.rdBtnNewAcct).xyw(3, 9, 11)

            .add(this.dateRanger.getChoiceLabel())
                .labelFor(this.comboBoxDateRange).xy(1, 11)
            .add(this.comboBoxDateRange).xyw(3, 11, 11)

            .add(this.dateRanger.getStartField()).xyw(3, 13, 3)
            .add(localizer.getString("label_custom_date_to"))
                .labelFor(this.dateRanger.getEndField()).xyw(7, 13, 1)
            .add(this.dateRanger.getEndField()).xyw(9, 13, 5)

            .add(this.progressBar).xy(1, 15)
            .add(this.btnCancel)
                .xyw(8, 15, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT)
            .add(this.btnProceed).xyw(11, 15, 3)
            .build();

        this.setContentPane(this.jpanel);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.pack();
    }
}
