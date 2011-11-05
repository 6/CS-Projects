/*
 * File: CreateGestureListener.java
 * Author: Martha Witick, Peter Graham
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * On action, create a gesture from the currently selected sounds and add it to
 * the given JControlPanel as an option.
 * 
 * @author Peter Graham
 * @author Martha Witick
 */
public class CreateGestureListener implements ActionListener {

    /** The CompositionPanel to show the message dialogs from */
    private CompositionPanel compositionPanel;
	
    /** The JControlPanel to add the new gesture to. */
    private JControlPanel controlPanel;
    
    /** The model for accessing selected sounds and delays */
    private CompositionModel model;

    /**
     * Initialize and set fields.
     *
     * @param comp CompositionPanel to show message dialogs from
     * @param jcp JControlPanel to add new gestures to
     * @param m the CompositionModel for accessing selected sounds and delays
     */
    public CreateGestureListener( CompositionPanel comp, JControlPanel jcp, 
                                 CompositionModel m ) {
    	compositionPanel = comp;
        controlPanel = jcp;
        model = m;
    }

    /**
     * Lets user creates a new user defined gesture out of the selected labels,
     * or displays an error message if no labels are selected.  Asks the user
     * to name the gesture; if the name is already used or no name is entered,
     * display an error, otherwise, add the gesture to the given JControlPanel
     * as an option.
     * 
     * @param e ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent e) {
        List<SoundResource> selectedSounds = model.getSelected();

        if(selectedSounds.size() <= 0) {
        	JOptionPane.showMessageDialog(compositionPanel,
            "Please select something before creating a gesture!");
        	return;
        }
        
        String gestureName = JOptionPane.showInputDialog(
                    "Please enter a name for the gesture:");

        if(gestureName == null || model.isGestureNameTaken(gestureName) ) {
        	// warn the user and do nothing
        	JOptionPane.showMessageDialog( compositionPanel,
                    "Gesture names must not be already taken or empty. " +
                    "Please try another name." );
        	return;
        }
        
        // the name is valid, add the gesture
    	GestureSound gesture = new GestureSound(selectedSounds, 
    			model.getSelectedRelativeDelays());
    	SoundResource wrappedGesture = new SoundResource(gestureName, gesture, 
    			0, 0);
        controlPanel.addUserDefinedGesture(gestureName, wrappedGesture, model);
    }
}