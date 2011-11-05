/*
 * File:         CompositionPanel.java
 * Authors:      Martha Witick, Peter Graham
 * Prev Authors: Leah Perlmutter, Katherine Smith, Will O'Brien
 * Class:      	 CS 361, Fall 2010
 * Project:      8
 * Date:       	 10 December
 */
package project8;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * A subclassed JPanel that keeps track of the labels on it. It is the 'view' of
 * our MVC design.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class CompositionPanel extends JPanel {

    /** A yellow LineBorder border for the labels to share. */
    private LineBorder yellowBorder;
    
    /** A black LineBorder border for the labels to share. */
    private LineBorder blackBorder;
    
    /** 
     * Holds the labels shown in the view, where the key is a label's associated
     * SoundResource.
     */
    private HashMap<SoundResource, JLabel> labels;
    
    /**
     * Select things on this list when add is called
     */
    private List<SoundResource> selectOnAdd;
    
    /** 
     *  Hold a list of guidelines to refresh - is a list so that guidelines are
     *  easy to access */
    private List<JLabel> guidelines;

    /** Constructor - initializes fields. */
    public CompositionPanel() {
        yellowBorder = new LineBorder(Color.YELLOW, 3);
        blackBorder = new LineBorder(Color.BLACK, 1);
        
        labels = new HashMap<SoundResource, JLabel>();
        selectOnAdd = new ArrayList<SoundResource>();
        
        guidelines = new ArrayList<JLabel>();
    }
    
    /**
     * Creates a new label on the view from the given SoundResource. It will be
     * selected by default.
     * 
     * @param soundResource - the SoundResource to create the label from.
     * @param resolution - the resolution of the composition panel.
     */
    public void updateAdd(SoundResource soundResource, int resolution) {

    	// check if SoundResource has an icon or not
    	JLabel newLabel;
    	if(soundResource.getIcon() != null) {
            newLabel = new JLabel(soundResource.getIcon());
    	}
    	else {
            newLabel = new JLabel(soundResource.getName());
    	}
    	
    	// Set default label properties
    	newLabel.setOpaque(true);
    	newLabel.setBackground(Color.white);
    	newLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	newLabel.setBorder(blackBorder);
    	
    	// Set size and location
    	newLabel.setSize(soundResource.getDuration() / resolution, 
            Constants.LABEL_HEIGHT);
    	newLabel.setLocation(soundResource.calculatePixelFromAbsolute(
    			resolution));
    	labels.put(soundResource, newLabel);
    	
    	add(newLabel, 0);
    	
    	setWidthIfGreater(newLabel.getX() + newLabel.getWidth());
    	
    	// If we're waiting to be selected, select and stop waiting
    	if( selectOnAdd.contains(soundResource) ) {
            selectOnAdd.remove(soundResource);
            updateSelected(soundResource, true);
    	}
    	
    	// Show changes
    	updateUI();
    }
    
    /**
     * Deletes the label associated with the given SoundResource.
     * 
     * @param soundResource - a SoundResource that should be deleted
     */
    public void updateRemove(SoundResource soundResource) {
    	remove( labels.get(soundResource) );
    	labels.remove(soundResource);
    	
    	// Show changes
    	updateUI();
    }
    
    /**
     * Deletes the labels associated with the given SoundResource.
     * 
     * @param soundResources - a list of SoundResource that should be deleted
     */
    public void updateRemove(List<SoundResource> soundResources) {
    	for(SoundResource soundResource : soundResources) {
            this.updateRemove(soundResource);
    	}
    }
    
    /**
     * Updates the position of a given SoundResource. If a label goes off right
     * edge of screen, update the horizontal scrollbar.
     * 
     * @param soundResource - a SoundResources to move.
     * @param resolution - the resolution of the composition panel.
     */
    public void updateLocation(SoundResource soundResource, int resolution) {
        // get label that represents this soundResource and move it
        JLabel curLabel = this.labels.get(soundResource);
        curLabel.setLocation(soundResource.calculatePixelFromAbsolute(
        		resolution));

        // set its new width based on resolution
        int newWidth = soundResource.getDuration() / resolution;
        curLabel.setSize(newWidth, curLabel.getHeight());

        this.setWidthIfGreater( curLabel.getX() + curLabel.getWidth() );
    }
    
    /**
     * Updates the position of given SoundResources. If a label goes off right
     * edge of screen, update the horizontal scrollbar.
     * 
     * @param soundResources - a list of SoundResources that should be moved.
     * @param resolution - the resolution of the composition panel.
     */
    public void updateLocation(List<SoundResource> soundResources, 
            int resolution) {
    	for(SoundResource soundResource : soundResources) {
            this.updateLocation(soundResource, resolution);
    	}
    }
    
    /**
     * Updates the selection of the given SoundResource.
     * 
     * @param soundResource - a SoundResource to select or deselect
     * @param select - true if selecting label, false if deselecting
     */
    public void updateSelected(SoundResource soundResource, boolean select) {
    	
        // get label that represents this soundResource and change its border
        JLabel curLabel = this.labels.get(soundResource);

        // if the label is null, add it to selectOnAdd and skip selecting it
        if( curLabel == null ) {
            selectOnAdd.add(soundResource);
        }
        // else, change the border
        else {
            if(select) {
                curLabel.setBorder(this.yellowBorder);
            }
            else {
                curLabel.setBorder(this.blackBorder);
            }
        }
        
        // Show changes
        updateUI();
    }
    
    /**
     * Updates the selections of the given SoundResources.
     * 
     * @param soundResources - list of SoundResources to select or deselect
     * @param select - true if selecting labels, false if deselecting
     */
    public void updateSelected(List<SoundResource> soundResources, 
            boolean select) {
    	for(SoundResource soundResource : soundResources) {
            this.updateSelected(soundResource, select);
    	}
    }
    
    /**
     * Returns the SoundResource associated with the given JComponent. If no 
     * SoundResource is associated with component, returns null.
     * 
     * @param component the component (JLabel) to look for
     * @return SoundResource associated with JComponent, or null if not found
     */
    public SoundResource getSoundResourceFromComponent(JComponent component) {
    	SoundResource componentSoundResource = null;
    	for(SoundResource soundResource : this.labels.keySet()) {
            if(this.labels.get(soundResource) == component) {
                componentSoundResource = soundResource;
                break;
            }
    	}
    	return componentSoundResource;
    }
    
    /**
     * If snapTo is enabled, draw vertical black single-pixel wide guide lines
     * on this panel guideDistance far from each other. Otherwise, remove all
     * drawn guide lines.
     * 
     * @param isSnapToEnabled determine whether to draw guidelines
     * @param guideDistance between guidelines (pixels)
     */
    public void updateSnapToGuides(boolean isSnapToEnabled, int guideDistance) {
    	// take the easy way out and remove all current lines from the panel
    	for( JLabel line : guidelines ) {
            remove(line);
    	}
    	guidelines.clear();
    	
    	// If snap to is enabled, draw more lines guideDistance apart
    	if( isSnapToEnabled ) {

            int newLineLocation = 0;
            while( newLineLocation < getWidth() ) {

                // draw new line and add
                JLabel newLine = new JLabel();
                newLine.setSize( Constants.BAR_WIDTH, getHeight() );
                newLine.setLocation(newLineLocation, 0);
                newLine.setOpaque(true);
                newLine.setBackground(Color.BLACK);

                add(newLine, -1);
                guidelines.add(newLine);

                // add width to newLineLocation
                newLineLocation += guideDistance;
            }
        }
    	updateUI();
    }
    
    /**
     * If the given x coordinate is greater than this panel's current width, 
     * resize the preferred size of this panel accordingly.
     * 
     * @param x Coordinate to test against this panel's current width. 
     */
    private void setWidthIfGreater( int x ) {
        if( x > this.getWidth() ) {
            this.setPreferredSize( new Dimension( x, this.getHeight() ));
            
            // add more guidelines if we need them
            if( guidelines.size() > 1 ) {
            	// get the distance between guidelines
            	int distance = 
                  Math.abs(guidelines.get(1).getX() - guidelines.get(0).getX());
            	
            	updateSnapToGuides( true, distance );
            }
            updateUI();
        }
    }
}