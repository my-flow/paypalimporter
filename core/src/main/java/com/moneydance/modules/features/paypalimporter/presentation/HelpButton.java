// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.presentation;

import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.jcip.annotations.Immutable;

/**
 * @author Florian J. Breunig
 */
@Immutable
final class HelpButton extends JButton {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("nullness")
    HelpButton() {
        super();
        final Image image = Helper.INSTANCE.getSettings().getHelpImage();
        this.setIcon(new ImageIcon(image));
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
}
