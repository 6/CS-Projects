/*
 * File:    DeleteListener.java
 * Author:  Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter
 * Class:   CS 360, Fall 2010
 * Project: 8
 * Date:    6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Delete selected items on an CompositionModel when an
 * ActionEvent happens on the panel. Knows the CompositionModel.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class DeleteListener implements ActionListener {

    /** The model controller to delete items from. */
    private CompositionModelController modelController;

    /**
     * Constructor that takes in a CompositionModel and sets this object's
     * panel to delete items from.
     * 
     * @param cmc CompositionModelController to give delete to.
     */
    public DeleteListener(CompositionModelController cmc) {
        modelController = cmc;
    }

    /** 
     * Delete selected items from the model.
     * 
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
        modelController.deleteSelected();
    }
}