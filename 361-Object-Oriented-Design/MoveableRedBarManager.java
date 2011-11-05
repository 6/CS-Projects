/*
 * File: MovableRedBarManager.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Leah Perlmutter, Katherine Smith
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Manages a JLabel (initialized to a tall, red bar) that can sweep across the 
 * given CompositionPanel inside of its own thread.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class MoveableRedBarManager extends JLabel {

    /** Panel on which red bar can appear. */
    private CompositionPanel panel;
    
    /** Model controller to get resolution from. */
    private CompositionModelController modelController;

    /** X coordinate of the panel on the bar. */
    private int barLocation;

    /** 
     * Initialize fields and construct the bar. 
     * 
     * @param p CompositionPanel to draw the red bar on.
     * @param cmc CompositionModelController to get the resolution from
     */
    public MoveableRedBarManager(CompositionPanel p, 
            CompositionModelController cmc) {
        panel = p;
        modelController = cmc;
                
        setSize(Constants.BAR_WIDTH, panel.getHeight());
        setLocation(0, 0);
        setOpaque(true);
        setBackground(Color.RED);
    }

    /** 
     * Remove bar from panel.
     */
    public void removeBar() {
        panel.remove(this);
    }

    /** 
     * Sweep the red bar across the panel and make it disappear when it 
     * reaches endPosition. Run the bar separately from the sound because it 
     * makes the sound playing method too complicated otherwise.
     * 
     * @param endPosition int of the red bar's final x position.
     */
    public void sweepAcross(final int endPosition){
    	// Add the red bar to the panel
    	panel.add(this, 0);

        // Move bar 1 pixel and sleep for MS_PER_PIXEL ms
        // until bar reaches end position
        new Thread()
        {
            public void run() {
                for(barLocation=0; barLocation<endPosition; barLocation++) {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run(){
                            setLocation(barLocation, 0);
                            
                            // adjust size of bar for window resize during play
                            setSize(Constants.BAR_WIDTH,
                                            panel.getHeight() );
                        }
                    });
                    try {
                    	// Sleep for the number of ms per pixel
                        Thread.sleep( modelController.getResolution() );
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // remove bar once it reaches end position
                removeBar();
                panel.updateUI();
            }
        }.start();
    }
}