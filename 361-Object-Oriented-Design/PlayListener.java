/*
 * File:       PlayListener.java
 * Author:     Martha Witick, Peter Graham
 * Previous Authors: Peter Williams, Katherine Smith, Leah Perlmutter
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides an action listener that plays the Sounds with the appropriate delay
 * and sets the MoveableRedBarManager moving.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class PlayListener implements ActionListener
{
    /** The CompositionModelController to play sounds from*/
    private CompositionModelController modelController;

    /** The MoveableRedBarManager that control the bar. */
    private MoveableRedBarManager playBar;

    /** 
     * Initialize the fields.
     * 
     * @param cmc CompositionModelController to play sounds from.
     * @param b MovableRedBarManager to play the bar.
     */
    public PlayListener(CompositionModelController cmc, 
            MoveableRedBarManager b) {
        modelController = cmc;
        playBar = b;
    }

    /**
     * Start playing on the ActionEvent. Sweep a red bar across the panel and
     * play sound associated with any labels on the panel.
     * 
     * @param e ActionEvent to start the method on.
     */
    public void actionPerformed(ActionEvent e) {
        play();
    }

    /**
     * Sweep a red line across the panel and play the sounds associated
   	 * with any labels on the panel.
   	 */
    private void play()
    {
    	// play all sounds and get the total duration
    	modelController.play();
    	
    	int rightmostX = modelController.duration();

    	// adjust for resolution
    	rightmostX = rightmostX / modelController.getResolution();
    	
        // Put the bar on the panel and sweep it across.
        // Freeze panel while sweeping so user can't move icons during play.
        modelController.setFrozen(true);
        playBar.sweepAcross(rightmostX);
        modelController.setFrozen(false);
    }
}
