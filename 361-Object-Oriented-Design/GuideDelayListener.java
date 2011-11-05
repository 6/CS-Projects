/*
 * File:       GuideDelayListener.java
 * Author:     Martha Witick, Peter Graham
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       4 December
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * Provides an action listener for setting a new guide delay.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class GuideDelayListener implements ActionListener {

    /** The CompositionPanel to show the message dialogs from */
    private CompositionPanel compositionPanel;
	
    /** Controller to make changes to */
    private CompositionModelController controller;

    /**
     * Initialize fields.
     *
     * @param comp CompositionPanel to show message dialogs from
     * @param cmc the CompositionModelController
     */
    public GuideDelayListener(CompositionPanel comp,
            CompositionModelController cmc) {
        compositionPanel = comp;
        controller = cmc;
    }

    /**
     * Sets a new guide delay in ms.
     * Note: if an invalid delay is entered, shows an error message.
     *
     * @param actionEvent - ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        // default value of input field is delay that's currently used
        String currentGuideDelay = Integer.toString(controller.getGuideDelay());

        String newDelay = JOptionPane.showInputDialog(compositionPanel,
        "Enter a guide delay in ms:", currentGuideDelay);

        if(newDelay == null) {
            // user pressed cancel
            return;
        }

        try {
            int newGuideDelay = Integer.parseInt(newDelay);
            if(newGuideDelay < 1) {
                JOptionPane.showMessageDialog( compositionPanel,
                    "Guide delay must be a positive integer.");
            }
            else {
                controller.setGuideDelay(newGuideDelay);
                controller.updateSnapToGuides();
            }
        }
        catch(NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog( compositionPanel,
                "Guide delay must an integer.");
        }
    }
}