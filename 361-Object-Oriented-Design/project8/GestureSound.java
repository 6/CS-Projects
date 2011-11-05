/*
 * File:       GestureSound.java
 * Authors:    Martha Witick, Peter Graham
 * Class:      CS 361, Fall 2010
 * Project:    9
 * Date:       8 November
 */
package project8;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a user-defined sampled sound file made up of a series of
 * ClipSounds.
 * It knows how to play the sounds and it knows the sounds' durations.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class GestureSound implements Sound {
    
    /** Holds the SoundResources associated with this gesture */
    private List<SoundResource> sounds;
    
    /**
     * Creates the gesture sound and initializes fields.
     *
     * @param newSounds - the sounds to include in the gesture
     * @param newDelays - the relative delays (in MS) associated with the Sounds
     */
    public GestureSound(List<SoundResource> newSounds, List<Integer> newDelays){

    	sounds = new ArrayList<SoundResource>();
    	
        // Note that all sounds must be wrapping master SoundResources to work
    	for( int i=0; i<newSounds.size(); i++ ) {

            // get the master sound resource
            SoundResource master = (SoundResource)(newSounds.get(i).getSound());

            sounds.add( new SoundResource( master, newDelays.get(i),
                newSounds.get(i).getTrack()) );
    	}
    }

    /**
     * Plays each Sound associated with this gesture.
     */
    public void play() {
        play(0);
    }

    /**
     * Sleeps for the given delay and then plays each Sound associated with this
     * gesture.
     *
     * @param delay - the number of milliseconds it delays before playing.
     */
    public void play(final int delay) {
    	
    	for( SoundResource sound : sounds ) {
            sound.play( delay );
    	}
    }

    /**
     * Get the duration of the sound in milliseconds.
     * 
     * @return the number of milliseconds the sound plays
     */
    public int getDuration()
    {
    	// Store the biggest delay + sound duration
    	int duration = 0;
    	
    	// Determine the width of this label by finding the sound ending last.
    	for( SoundResource sound : sounds ) {
            int possibleWidth = sound.getDelay() + sound.getDuration();

            // Set width to delay + sound length if it's greater than width
            if( duration < possibleWidth ){
            	duration = possibleWidth;
            }
    	}
    	
    	return duration;
    }
    
    /**
     * Gets the list of sounds associated with this gesture.
     * @return list of sounds
     */
    public List<SoundResource> getSounds() {
    	return sounds;
    }
}