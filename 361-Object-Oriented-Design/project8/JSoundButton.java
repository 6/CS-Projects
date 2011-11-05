/*
 * File:       JSoundButton.java
 * Authors:	   Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * A subclassed JButton that has an Icon or String and an associated Sound.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class JSoundButton extends JButton {

    /** Sound associated with the button. */
    private Sound sound;

    /** 
     * Construct the button with an icon and a sound.
     * 
     * @param icon ImageIcon displayed on the middle front of the button.
     * @param snd Sound holding its own unique sound to play.
     */
    public JSoundButton(Icon icon, Sound snd) {
        super(icon);
        this.sound = snd;
    }
    
    /** 
     * Construct the button with a string and a sound.
     * 
     * @param name String displayed on the middle front of the button.
     * @param snd Sound holding its own unique sound to play.
     */
    public JSoundButton(String name, Sound snd) {
        super(name);
        this.sound = snd;
    }

    /**
     * @return the Sound associated with this button.
     */
    public Sound getSound() {
        return this.sound;
    }

    /** Play the Sound associated with this button. */
    public void playSound() {
    	this.sound.play();
    }
}