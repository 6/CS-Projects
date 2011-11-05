/*
 File:       SoundResource.java
 Author:     Martha Witick, Peter Graham
 Class:      CS 361, Fall 2010
 Project:    8
 Date:       5 November
*/

package project8;

import java.awt.Point;
import javax.swing.Icon;

/**
 * Holds a Sound to play and an image to show.
 * 
 * @author pagraham
 * @author mmwitick
 */
public class SoundResource implements Sound {

    /** Icon for objects representing this data to show. */
    private Icon icon;

    /** String for objects representing this data to show. */
    private String name;
    
    /** Filename of sound */
    private String soundFilename;
    
    /** Filename of icon */
    private String iconFilename;
    
    /** Sound to play */
    private Sound sound;
    
    /** Delay in ms to play sound at */
    private int delay;
    
    /** Track of sound */
    private int track;

    /**
     * Initialize and set fields appropriately.
     * Note: this is not a clone constructor.
     *
     * @param soundResource soundResource to associate with
     * @param newDelay delay to play sound at
     * @param newTrack track of sound
     */
    public SoundResource(SoundResource soundResource, int newDelay, 
            int newTrack) {
        icon = soundResource.getIcon();
        name = soundResource.getName();
        sound = soundResource; // assume we're handed the master sound resource
        delay = newDelay;
        track = newTrack;

        soundFilename = soundResource.getSoundFilename();
        iconFilename = soundResource.getIconFilename();
    }
    
    /**
     * Initialize and set fields appropriately.
     *
     * @param newIcon icon to associate with
     * @param newSound sound to associate with
     * @param newDelay delay to play sound at
     * @param newTrack track of sound
     * @param sFilename filename of sound
     * @param iFilename filename of icon
     */
    public SoundResource( Icon newIcon, Sound newSound, int newDelay, 
            int newTrack, String sFilename, String iFilename ) {
        icon = newIcon;
        name = "";
        sound = newSound;
        delay = newDelay;
        track = newTrack;
        
        soundFilename = sFilename;
        iconFilename = iFilename;
    }

    /**
     * Initialize and set fields appropriately.
     *
     * @param newName String to associate with
     * @param newSound sound to associate with
     * @param newDelay delay to play sound at
     * @param newTrack track of sound
     */
    public SoundResource( String newName, Sound newSound, int newDelay,
    		int newTrack ) {
        icon = null;
        name = newName;
        sound = newSound;
        delay = newDelay;
        track = newTrack;
        
        soundFilename = "";
        iconFilename = "";
    }

    /**
     * Return this object's icon.
     *
     * @return Icon associated with this object
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Return this object's associated sound.
     *
     * @return Sound associate with this object
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Returns this object's name.
     *
     * @return the String to associated with this object
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns this sound's filename.
     * 
     * @return the String of this sound's filename
     */
    public String getSoundFilename() {
    	return soundFilename;
    }
    
    /**
     * Returns this icon's filename.
     * 
     * @return the String of this icon's filename
     */
    public String getIconFilename() {
    	return iconFilename;
    }

    /**
     * Returns the sound's duration in milliseconds.
     *
     * @return the duration of the sound (in MS) associated with this object
     */
    public int getDuration() {
        return this.getSound().getDuration();
    }
    
    /**
     * Return the delay of this sound in milliseconds.
     * 
     * @return int delay (ms)
     */
    public int getDelay() {
    	return delay;
    }
    
    /**
     * Return the track of this sound.
     *
     * @return int track
     */
    public int getTrack() {
    	return track;
	}

    /**
     * Return this object's absolute position.
     *
     * @return Point absolute position
     */
    public Point getAbsolutePosition() {
        return new Point( delay, track );
    }

    /**
     * Set this object's absolute position.
     *
     * @param Point new absolute position of this object
     */
    public void setLocation(Point newAbsolutePosition ) {
        delay = (int)newAbsolutePosition.getX();
        track = (int)newAbsolutePosition.getY();
    }

    /**
     * Set this object's absolute position.
     *
     * @param x new absolute x position of this object
     * @param y new absolute x position of this object
     */
    public void setLocation(int x, int y) {
        this.setLocation(new Point( x, y ));
    }
	
    /**
     * Convert this object's absolute location to a pixel location.
     * 
     * @param resolution Resolution to convert with
     * @return Point pixel location
     */
    public Point calculatePixelFromAbsolute( int resolution ) {
    	
    	// pixel position = absolute position / resolution
    	Point pixelLocation = new Point( delay / resolution, track );
    	
    	return pixelLocation;
    }

    /** Plays the sound associated with this object. */
    public void play() {
            sound.play( delay );
    }
    
    /**
     * Plays the sound associated with this object with the given delay added
     * to its own delay.
     * 
     * @param extraDelay to add to own delay
     */
    public void play( int extraDelay ) {
    	sound.play( delay + extraDelay );
    }
}