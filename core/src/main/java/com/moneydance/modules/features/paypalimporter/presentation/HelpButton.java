// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.presentation;

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
    HelpButton(final Image image) {
        super();
        this.setIcon(new ImageIcon(image));
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
}
