/*
 * File: RepeaterListener.java
 * Authors: Martha Witick, Peter Graham
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 4 December
 */

package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * On ActionEvent, repeat the selected sounds a user-given n amount of times.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class RepeaterListener implements ActionListener {

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
    public RepeaterListener(CompositionPanel panel,
            CompositionModelController cmc) {
        compositionPanel = panel;
        controller = cmc;
    }

    /**
     * On event,  prompt the user for n number of repeats and repeat the model's
     * selected sounds n times.
     *
     * @param e ActionEvent to do stuff on
     */
    public void actionPerformed(ActionEvent e) {
        String entry = JOptionPane.showInputDialog(compositionPanel,
            "Enter number of times to repeat selected sounds:", 1);

        String spaceEntry = JOptionPane.showInputDialog(compositionPanel,
            "Enter time in ms to add to the end of each group of sounds:", 0);

        if(entry == null) {
            // user pressed cancel
            return;
        }

        try {
            int repeats = Integer.parseInt(entry);
            int space = Integer.parseInt(spaceEntry);
            if(repeats < 0 || space < 0 ) {
                JOptionPane.showMessageDialog( compositionPanel,
                    "Please enter a positive integer.");
            }
            else {
                // repeat the selected sounds n times
                controller.repeatSounds(repeats, controller.getSelected(),
                    space);
            }
        }
        catch(NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog( compositionPanel,
                "Please enter a positive integer.");
        }
    }
}