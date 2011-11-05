/*
 * File: PaletteSelectListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Leah Perlmutter, Katherine Smith
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import javax.swing.*;
import java.awt.event.*;

/**
 * Change visible panel when user selects something on the object this listener
 * is attached to.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class PaletteSelectListener implements ActionListener {

    /** The control panel to swap palettes on. */
    private JControlPanel panel;

    /** 
     * Initialize the JControlPanel field with p.
     * 
     * @param p JControlPanel to attach to p.
     */
    public PaletteSelectListener(JControlPanel p) {
        panel = p;
    }

    /** 
     * Change visible panel based on user selection.
     * 
     * @param ae ActionEvent to trigger method.
     */
    public void actionPerformed(ActionEvent ae) {

        panel.remove(panel.getComponentCount() - 1); //remove the old icon panel

        JPanel newPanel = panel.getSelectedPalette();
        panel.add(newPanel);

        panel.selectFirstButton();

        panel.updateUI();
    }
}