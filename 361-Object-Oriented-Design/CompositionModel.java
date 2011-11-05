/*
 File:       CompositionModel.java
 Author:     Martha Witick, Peter Graham
 Class:      CS 361, Fall 2010
 Project:    8
 Date:       10 December
*/
package project8;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Holds the sounds and images shown by the CompositionPanel and a list of the
 * currently selected soundResources. Notifies the panel when changes are made.
 * 
 * @author pagraham
 * @author mmwitick
 */
public class CompositionModel {
	
    /** Keep a reference to this model's view. */
    private CompositionPanel panel;

    /** Holds a list of soundResources to show in a view. */
    private List<SoundResource> soundResources;
    
    /** 
     * Hold a hashmap of master soundResources containing sounds and icons.
     * The String key is category string of the sound's file.
     */
    private HashMap<String, List<SoundResource>> masterResources;
    
    /** 
     * Hold a convenience hashmap mapping sound names to master sounds.
     * The String key is the sound's filename. */
    private HashMap<String, SoundResource> soundTable;
    
    /** 
     * Hold a convenient hashmap with strings mapping to user defined sounds.
     * The String key is its corresponding gesture's name.
     */
    private HashMap<String, SoundResource> gestureTable;
	
    /** 
     * Holds the soundResources that are selected. Needs to be ordered so that 
     * list of soundResources matches up with list of offsets in the drag 
     * listener when dragging.
     */
    private List<SoundResource> selected;
    
    /**
     * Constructor for the console version - initializes fields, sets panel to
     * null, sets elements needed by the GUI to null.
     */
    public CompositionModel() {
    	soundResources = new ArrayList<SoundResource>();
        selected = null;
        panel = null;
        
        loadResources(false);
    }
    
    /** 
     * Constructor - initializes fields. 
     * 
     * @param cPanel CompositionPanel to edit
     */
    public CompositionModel( CompositionPanel cPanel ) {
    	soundResources = new ArrayList<SoundResource>();
        selected = new ArrayList<SoundResource>();
        panel = cPanel;
        
        loadResources(true);
    }
    
    /**
     * Return a List<soundResource> of all the current soundResources
     * 
     * @return List<soundResource> of all current soundResources
     */
    public List<SoundResource> getSoundResources() {
    	return soundResources;
    }
    
    /**
     * Return a HashMap<String, List<SoundResource>> of loaded SoundResources
     * 
     * @return List<soundResource> of all current soundResources
     */
    public HashMap<String, List<SoundResource>> getMasterResources() {
    	return (HashMap<String, List<SoundResource>>)masterResources.clone();
    }
    
    /** 
     * Return a List<SoundResource> containing the selected soundResources. 
     * 
     * @return List<SoundResource> - selected soundResources.
     */
    public List<SoundResource> getSelected() {
        return selected;
    }
    
    /**
     * Add the given custom SoundResource to the User Defined space in the
     * Master Resources. The SoundResource should be a master gesture.
     * 
     * @param soundResource that represents a gesture
     */
    public void addUserDefinedGesture(SoundResource soundResource) {
    	
    	// Add to the gesture lookup table
    	gestureTable.put( soundResource.getName(), soundResource );
    }
    
    /**
     * When given a String name, return the appropriate master gesture 
     * SoundResource if it exists.
     * 
     * @param name of gesture to check
     * @return SoundResource of gesture if a match is found, null otherwise
     */
    public SoundResource getResourceFromGestureName( String name ) {
    	return gestureTable.get(name);
    }
    
    /**
     * When given a String filename of a sound, return the appropriate master 
     * SoundResource if it exists.
     *
     * @param filename of a sound to check
     * @return SoundResource of sound if it matches, null otherwise
     */
    public SoundResource getResourceFromSoundFilename( String filename ) {
    	return soundTable.get(filename);
    }
    
    /**
     * Check to see if the given gesture name is taken.
     * 
     * @param name to check
     * @return boolean true if the gesture name is taken
     */
    public boolean isGestureNameTaken(String name) {

    	return gestureTable.containsKey(name);
    }
    
    /**
     * Adds the given soundResource to this object. Only add if this object does 
     * not already have the given soundResource.
     * 
     * @param soundResource to add to this object.
     * @param resolution to adjust for in the view
     */
    public void add( SoundResource soundResource, int resolution ) {
    	if( !soundResources.contains(soundResource) ){
            soundResources.add( soundResource );
            
            //Check if the panel is null first
            if( panel != null ) {
                panel.updateAdd(soundResource, resolution);
            }
    	}
    }
    
    /**
     * Add the given List of soundResources to this model. Only add individual
     * soundResources if they are not already there.
     * 
     * @param newSoundResources to add
     * @param resolution to adjust for in the view
     */
    public void add( List<SoundResource> newSoundResources, int resolution ) {
    	for( SoundResource soundResource : newSoundResources ) {
    		add( soundResource, resolution );
    	}
    }
    
    /**
     * Remove the given soundResource from this object.
     * 
     * @param soundResource to remove
     */
    public void remove( SoundResource soundResource ) {
    	soundResources.remove( soundResource );
    	
    	panel.updateRemove(soundResource);   	
    }
    
    /**
     * Remove the given list of soundResources from this object.
     * 
     * @param removed SoundResources
     */
    public void remove( List<SoundResource> removed ) {
    	for( SoundResource soundResource : removed ) {
            remove( soundResource );
    	}
    }
    
    /**
     * Update the given soundResource's location in the panel.
     * 
     * @param soundResource to update location for
     * @param resolution to update panel with
     */
    public void updateLocation( SoundResource soundResource, int resolution ) {
    	panel.updateLocation(soundResource, resolution);
    }
    
    /**
     * Update the given soundResource's location in the panel.
     * 
     * @param soundResources to update location for
     * @param resolution to update panel with
     */
    public void updateLocation( List<SoundResource> soundResource,
            int resolution ) {
    	panel.updateLocation(soundResource, resolution);
    }
    
    /**
     * Replace the elements of selected with elements from the given List
     * newSelected.
     * 
     * @param newSelected List of soundResources whose elements replace 
     * 		selected's elements
     */
    public void setSelected( List<SoundResource> newSelected ) {
    	
    	// deselect everything in selected
    	clearSelection();
    	
    	// select everything in newSelected
    	for( SoundResource soundResource : newSelected ) {
            selectSoundResource(soundResource);
    	}
    }
    
    /** 
     * Sort through selected and return a list of relative delays with 0 at
     * the leftmost edge of the leftmost label.
     * 
     * @return List<Integer> of delays in ms
     */
    public List<Integer> getSelectedRelativeDelays() {
    	List<Integer> delays = new ArrayList<Integer>();
    	int leftmost = (int)selected.get(0).getDelay();
    	
    	// find the leftmost edge
    	for(SoundResource soundResource : selected) {
            // if this s's x is less than our leftmost, set our leftmost
            if( (int)soundResource.getDelay() < leftmost ) {
                leftmost = (int)soundResource.getDelay();
            }
    	}	
    	
    	// knowing the leftmost edge, get delays in ms for all other labels
    	for(SoundResource soundResource : selected) {

            int delayOffset = ((int)soundResource.getDelay() - leftmost);
            
            delays.add( delayOffset );
    	}
    	return delays;
    }
    
    /**
     * Sort through selected and return a list of the sounds belonging to each 
     * label corresponding to the label's position with the label's delays.
     * 
     * @return List<Sound> containing references to Sounds corresponding to the
     * List returned with getDelays.
     */
    public List<Sound> getSelectedSounds() {
    	List<Sound> sounds = new ArrayList<Sound>();
    	
    	// add sounds from each label
    	for(SoundResource sound : selected) {
            sounds.add(sound.getSound());
    	}
    	return sounds;
    }
    
    /**
     * Return the duration in ms of the current composition.
     * 
     * @return int total duration is ms of all played sounds (time play ends).
     */
    public int duration() {
    	
    	return duration(soundResources);
    }
    
    /**
     * Return the duration in ms of the given composition.
     * 
     * @param sounds to find duration for
     * @return int total duration is ms of all played sounds (time play ends).
     */
    public int duration( List<SoundResource> sounds ) {
    	// Store the biggest delay + sound duration, also the start
    	// Need to preserve whitespace
    	int duration = 0;
    	int leftmost = sounds.get(0).getDelay(); 
    	
    	// find the leftmost edge (start) of the group of sounds
    	for(SoundResource soundResource : sounds) {
            // if this s's x is less than our leftmost, set our leftmost
            if( (int)soundResource.getDelay() < leftmost ) {
                leftmost = (int)soundResource.getDelay();
            }
    	}
    		
        // go through the given sounds and get their durations
        for ( SoundResource soundResource : sounds ) {

            // Calculate total duration
            int rightEdge = (soundResource.getDelay() - leftmost) +
                    (soundResource.getDuration());

            // update the rightmost edge of the rightmost label
            if( rightEdge > duration ) {
                    duration = rightEdge;
            }
        }
    	
    	return duration;
    }
    
    /**
     * Play all sounds in the model with their given delays.
     */
    public void play() {
    	
        // go through all the soundResources and play them
        for ( SoundResource soundResource : soundResources ) {
        	
            // start a sound for each soundResource with the proper delay
            soundResource.play();
        }
    }
    
    /**
     * Clear all soundResources, selected, edits, etc. to restore this model to
     * a like new state.
     */
    public void clearAllChanges() {
    	selected.clear();
    	remove( new ArrayList<SoundResource>(soundResources) );
    	gestureTable.clear();
    }

    /** 
     * Adds the given soundResource to selected.
     * Doesn't add the soundResource if it's already selected.
     * 
     * @param soundResource soundResource to select.
     */
    public void selectSoundResource(SoundResource soundResource) {
        if( ! selected.contains(soundResource) ) {
            selected.add(soundResource);
            
            panel.updateSelected(soundResource, true);
        }
    }

    /**
     * Remove a soundResource from selected.
     * 
     * @param soundResource soundResource to remove.
     */
    public void deselectSoundResource(SoundResource soundResource) {
        selected.remove(soundResource);
        
        panel.updateSelected(soundResource, false);
    }

    /**
     * Return whether the given soundResource is selected.
     * 
     * @param soundResource soundResource to test if selected or not.
     * @return Whether or not selected contains soundResource.
     */
    public boolean isSelected(SoundResource soundResource) {
        return selected.contains(soundResource);
    }

    /** 
     * Deselect all selected.
     */
    public void clearSelection() {
        panel.updateSelected( selected, false );
        selected.clear();
    }
    
    /** 
     * Delete all elements of selected from the CompositionPanel without
     * adding this edit to the undo manager.
     */
    public void deleteSelectedWithoutUndo() {
        remove(selected);
        selected.clear();
    }
    
    /**
     * Get the view to update its snap to guides accordingly
     * 
     * @param isSnapToEnabled draw lines on true, erase lines on false
     * @param guideDistance in pixels between guide lines
     */
    public void updateSnapToGuides(boolean isSnapToEnabled, int guideDistance) {
    	panel.updateSnapToGuides( isSnapToEnabled, guideDistance );
    }

    /**
     * Organize sounds so that the same sounds line up on the same y-values.
     *
     * @param resolution to draw the moved sounds at
     */
    public void organizeSounds( int resolution ) {

        // organize SoundResources by their sound
        HashMap<String,List<SoundResource>> organized =
                new HashMap<String,List<SoundResource>>();
        for(SoundResource soundResource : soundResources) {
            String soundName = soundResource.getSoundFilename();
            if(organized.containsKey(soundName)) {
                List<SoundResource> resources = organized.get(soundName);
                resources.add(soundResource);
                organized.put(soundName, resources);
            }
            else {
                List<SoundResource> resources = new ArrayList<SoundResource>();
                resources.add(soundResource);
                organized.put(soundName, resources);
            }
        }

        // change their y values
        int currentY = 0;
        for(String soundName : organized.keySet()) {
            List<SoundResource> organizedResources = organized.get(soundName);
            for(SoundResource soundResource : organizedResources) {
                soundResource.setLocation(soundResource.getDelay(), currentY);
            }
            updateLocation(organizedResources, resolution);
            currentY += Constants.LABEL_HEIGHT;
        }
    }

	/**
     * Loads the sounds and icons necessary to run the program.
     * Initializes masterResources.
     * 
     * @param isGUI is True when icons should be loaded
     */
    private void loadResources( boolean isGUI ) {
    	
    	// The String is the category (directory)
    	masterResources = new HashMap<String, List<SoundResource>>();
    	gestureTable = new HashMap<String, SoundResource>();
    	soundTable = new HashMap<String, SoundResource>();
    	
    	// Load appropriate directories
    	File baseAudioDirectory = new File("audio");
    	File baseIconDirectory = null;
    	
    	// only load icons if we're in the GUI
    	if( isGUI ) {
            baseIconDirectory = new File("icon");
    	}
    	
    	String[] categories = baseAudioDirectory.list();
    	
    	// For each category, load sounds and images
    	for( String category : categories ) {
    		
            // Initialize a category list
            List<SoundResource> soundRecs = new ArrayList<SoundResource>();

            // Load current category files
            File currentAudioDirectory = new File(baseAudioDirectory,
                System.getProperty("file.separator") + category);
            File currentIconDirectory = null;

            if( isGUI ) {
                currentIconDirectory = new File(baseIconDirectory,
                    System.getProperty("file.separator") + category);
            }

            // add a master SoundResource with each icon/sound pair
            String[] soundFiles = currentAudioDirectory.list();
            String[] iconFiles = null;
            
            if ( isGUI ) {
            	iconFiles = currentIconDirectory.list();
            }
            
            for (int j = 0; j < soundFiles.length; j++) {
            	// Load resource
                ClipSound currentSound = new ClipSound(
                    new File( currentAudioDirectory,
                        soundFiles[j]).getAbsolutePath());

                ImageIcon currentIcon = null;
                if( isGUI ) {
                    currentIcon = new ImageIcon(
                        new File( currentIconDirectory,
                            iconFiles[j]).getAbsolutePath());
                }
                
                SoundResource masterResource = null;
                
                if( isGUI ) {
                    masterResource = new SoundResource(currentIcon, 
                        currentSound, 0, 0, soundFiles[j], iconFiles[j] );
                }
                else {
                    masterResource = new SoundResource(currentIcon,
                        currentSound, 0, 0, soundFiles[j], null );
                }

                
                // Add the new master resource to the list
                soundRecs.add( masterResource );
                
                // Add the new master resource to soundTable for convenience
                soundTable.put( soundFiles[j], masterResource );
            }
            
            // Add soundRecs to the hashmap of master resources
            masterResources.put( category, soundRecs );
    	}
    }
}