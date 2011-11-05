/*
 * File:       Sound.java
 * Authors:    Martha Witick, Peter Graham
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       8 November
 */
package project8;

/**
 * A sound interface for playing sounds.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public interface Sound {
    
    /**
     * Plays the sound file associated with this object.
     */
    public void play();

    /**
     * Plays the sound with a given delay.
     *
     * @param delay - how many milliseconds to delay playing the sound
     */
    public void play(final int delay);

    /**
     * Get the duration of the sound in milliseconds.
     *
     * @return the number of milliseconds the sound plays
     */
    public int getDuration();
}
