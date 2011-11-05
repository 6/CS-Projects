/*
 * File:       SampleSoundListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors:     Katherine Smith, Leah Perlmutter
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 * Provides an ActionListener that plays the exampleSound.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class SampleSoundListener implements ActionListener {
    
    /** The JControlPanel that knows the exampleSound. */
    private JPanel panel;

    /** 
     * Initialize the panel field to one that knows the exampleSound.
     * 
     * @param p Set this JPanel panel to p.
     */
    public SampleSoundListener(JPanel p) {
        panel = p;
    }

    /**
     * Play the exampleSound and the actionEvent.
     * 
     * @param a ActionEvent to play the example sound on.
     */
    public void actionPerformed(ActionEvent a) {
        ((JSoundButton)((JControlPanel)panel).getSelectedButton()).playSound();
    }
}
