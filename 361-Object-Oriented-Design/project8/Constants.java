/*
 * File: Constants.java
 * Author: Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 10 December
 */
package project8;

/**
 * Contains constants visible to the whole BattleOfTheClipsounds application.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class Constants {

    /** Corresponds to icon dimension. */
    public static final int LABEL_HEIGHT =   36; // pixels

    /** Inverse of bar's speed at very low resolution. */
    public static final int VERY_LOW_MS_PER_PIXEL =   40;
    
    /** Inverse of bar's speed at low resolution. */
    public static final int LOW_MS_PER_PIXEL =   20;
    
    /** Inverse of bar's speed at medium resolution. (default) */
    public static final int MEDIUM_MS_PER_PIXEL =   10;
    
    /** Inverse of bar's speed at high resolution. */
    public static final int HIGH_MS_PER_PIXEL =   5;
    
    /** Inverse of bar's speed at very high resolution. */
    public static final int VERY_HIGH_MS_PER_PIXEL =   2;

    /** Initial width of main window. */
    public static final int FRAME_WIDTH  =  600; // pixels

    /** Initial height of main window. */
    public static final int FRAME_HEIGHT =  400; // pixels

    /** Initial screen x coordinate of window. */
    public static final int WINDOW_X     =  150; // pixels

    /** Initial screen y coordinate of window. */
    public static final int WINDOW_Y     =  350; // pixels

    /** Width of playbar. */
    public static final int BAR_WIDTH    =    1; // pixels
    
    /** Default distance between guides (ms) */
    public static final int DEFAULT_GUIDE_DELAY = 1000;
    
    /** Smallest allowed distance between guides */
    public static final int MIN_GUIDE_DISTANCE = 6;
}