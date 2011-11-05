/*
 * File: OrganizeSoundListener.java
 * Authors: Martha Witick, Peter Graham
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 4 December
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides an action listener for organizing sounds on the composition panel.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class OrganizeSoundListener implements ActionListener{

    /** Controller to send changes to */
    private CompositionModelController controller;

    /**
     * Initialize fields.
     *
     * @param cmc the CompositionModelController
     */
    public OrganizeSoundListener(CompositionModelController cmc) {
        controller = cmc;
    }

    /**
     * Organizes sounds so that the same sounds line up on the same y-values.
     *
     * @param actionEvent - ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent event) {
        controller.organizeSounds();
    }
}