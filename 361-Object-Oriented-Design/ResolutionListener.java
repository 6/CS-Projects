/*
 * File:       ResolutionListener.java
 * Authors:    Peter Graham, Martha Witick
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Switch the resolution of the given panel accordingly when the menu text
 * is selected.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class ResolutionListener implements ActionListener {
	
    /** the composition model controller to update */
    private CompositionModelController modelController;

    /**
     * Initialize the field that holds the CompositionModel.
     *
     * @param cmc CompositionModel controller to update the resolution for.
     */
    public ResolutionListener( CompositionModelController cmc ) {
        modelController = cmc;
    }

    /**
     * When this event is triggered, set resolution on the model accordingly.
     * .
     * @param e ActionEvent to get the resolution from.
     */
    public void actionPerformed(ActionEvent e) {
        String selected = e.getActionCommand();

        // Do nothing if panel is frozen
        if( !modelController.isFrozen() ) {
            if (selected.equals("High")) {
                modelController.setResolution(Constants.HIGH_MS_PER_PIXEL);
            }
            else if (selected.equals("Very High")) {
                modelController.setResolution(Constants.VERY_HIGH_MS_PER_PIXEL);
            }
            else if (selected.equals("Very Low")) {
                modelController.setResolution(Constants.VERY_LOW_MS_PER_PIXEL);
            }
            else if (selected.equals("Medium")) {
                modelController.setResolution(Constants.MEDIUM_MS_PER_PIXEL);
            }
            else {
                modelController.setResolution(Constants.LOW_MS_PER_PIXEL);
            }
            // redraw snap to guides
            modelController.updateSnapToGuides();
        }
    }
}