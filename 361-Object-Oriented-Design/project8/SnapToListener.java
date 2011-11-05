/*
 * File:       SnapToListener.java
 * Author:     Martha Witick, Peter Graham
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       4 December
 */
package project8;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;

/**
 * Provides an item listener that enables or disables the snap to guides.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class SnapToListener implements ItemListener{

    /** Controller to make changes to */
    private CompositionModelController controller;

    /**
     * Initialize fields.
     *
     * @param cmc the CompositionModelController
     */
    public SnapToListener(CompositionModelController cmc) {
        controller = cmc;
    }

    /**
     * Enables or disables the snap to guides.
     *
     * @param itemEvent - ItemEvent that triggers this method.
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        AbstractButton checkBox = (AbstractButton)itemEvent.getSource();
        controller.setIsSnapToEnabled(checkBox.isSelected());
        controller.updateSnapToGuides();
    }
}