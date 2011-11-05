/*
 File:       CompositionModelController.java
 Author:     Martha Witick, Peter Graham
 Class:      CS 361, Fall 2010
 Project:    8
 Date:       10 December
*/

package project8;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Talks to the listeners capable of editing its CompositionModel and holds the
 * UndoManager full of changes to its CompositionModel.
 * 
 * @author pagraham
 * @author mmwitick
 */
public class CompositionModelController {

    /** The model to keep track of and edit accordingly */
    private CompositionModel model;
	
    /**
     * The last saved array of selected soundResources before the last change.
     * Needs to be ordered to properly match with selected.
     */
    private List<SoundResource> currentLastSelected;

    /** Is true when we are currently using the selection box. */
    private boolean selecting;

    /** Is false when user is allowed to move soundResources. */
    private boolean frozen;
    
    /** Keep the current MS_PER_PIXEL resolution here. */
    private int resolution;
    
    /** When true, activate the snap to feature (snap labels to guides) */
    private boolean isSnapToEnabled;
    
    /** The distance in ms between the drawn guides */
    private int guideDelay;
    
    /** Create an UndoManager to handle undo and redo on this panel. */
    private UndoManager undoManager;
    
    /**
     * Initializes fields.
     * 
     * @param model CompositionModel to control
     */
    public CompositionModelController( CompositionModel m ) {
    	model = m;
        currentLastSelected = new ArrayList<SoundResource>();
        selecting = false;
        frozen = false;
        resolution = Constants.MEDIUM_MS_PER_PIXEL;
        isSnapToEnabled = false;
        guideDelay = Constants.DEFAULT_GUIDE_DELAY;
        undoManager = new UndoManager();
    }
    
    /**
     * Return the current model's selected sound resources.
     * 
     * @return a List<SoundResource> of this model's selected sound resources
     */
    public List<SoundResource> getSelected() {
    	return model.getSelected();
    }
    
    /**
     * Return the current milliseconds per pixel resolution.
     * 
     * @return int current milliseconds per pixel.
     */
    public int getResolution() {
    	return resolution;
    }
    
    /**
     * Return the UndoManager used for this panel.
     * 
     * @return undoManager UndoManager responsible for handling undo and redo
     * 		   in this panel.
     */
    public UndoManager getUndoManager() {
    	return undoManager;
    }
    
    /**
     * Return a List<SoundResource> of all the current soundResources
     * 
     * @return List<SoundResource> of all current soundResources
     */
    public List<SoundResource> getSoundResources() {
    	return model.getSoundResources();
    }
    
    /** 
     * Return List<SoundResource> containing previously selected soundResources. 
     * 
     * @return List<SoundResource> - previously selected soundResources.
     */
    public List<SoundResource> getCurrentLastSelected() {
    	return currentLastSelected;
    }
    

    /** 
     * Returns selecting - True means selection box is on panel. 
     * 
     * @return boolean - true if selection box on panel, false if not on panel
     */
    public boolean isSelecting() {
        return selecting;
    }
    
    /** 
     * Returns whether the panel is frozen - if true, the user cannot drag
     * selected soundResources.
     * 
     * @return frozen Indicates if the user can drag labels on this panel.
     */
    public boolean isFrozen() {
        return frozen;
    }
    
    /** 
     * Returns isSnapToEnabled.
     * 
     * @return isSnapToEnabled
     */
    public boolean isSnapToEnabled() {
    	return isSnapToEnabled;
    }
    
    /**
     * Return the current guide delay (ms).
     * 
     * @return current guide delay in ms
     */
    public int getGuideDelay() {
    	return guideDelay;
    }
    
    /**
     * Return whether the given SoundResource is selected.
     * 
     * @param soundResource SoundResource to test if selected or not.
     * @return Whether or not selected contains soundResource.
     */
    public boolean isSelected(SoundResource soundResource) {
        return model.isSelected(soundResource);
    }
    
    /**
     * Returns the sound resource with the lowest delay value among the given 
     * SoundResources. 
     * 
     * @param soundResources a list of SoundResources
     * @return the SoundResource with lowest delay
     */
    public SoundResource lowestDelay(List<SoundResource> soundResources) {
    	SoundResource lowest = soundResources.get(0);
    	for(SoundResource soundResource : soundResources) {
    		if(soundResource.getDelay() < lowest.getDelay()) {
    			lowest = soundResource;
    		}
    	}
    	
    	return lowest;
    }
    
    /** 
     * Sets boolean selecting to s.
     * @param s True when a selection box is selecting stuff.
     */
    public void setSelectionMode(boolean s) {
        selecting = s;
    }
    
    /** 
     * Set frozen to f. If true, the attached LabelDragListener will prevent
     * the user from dragging selected soundResources.
     * 
     * @param f Sets this frozen to f.
     */
    public void setFrozen(boolean f) {
        frozen = f;
    }
    
    /**
     * Set guide delay to given int (ms).
     * 
     * @param newGuideDelay in ms
     */
    public void setGuideDelay( int newGuideDelay ) {
    	guideDelay = newGuideDelay;
    }
    
    /**
     * Set isSnapeToEnabled to the given boolean.
     * 
     * @param enableSnapTo
     */
    public void setIsSnapToEnabled( boolean enableSnapTo ) {
    	isSnapToEnabled = enableSnapTo;
    }
    
    /**
     * Add the given edit to the UndoManager.
     * 
     * @param SelectEdit to add
     */
    public void addEdit( UndoableEdit edit ) {
    	undoManager.addEdit(edit);
    }
    
    /**
     * Adds the given soundResource to this model. Only add if this object does 
     * not already have the given soundResource.
     * 
     * @param soundResource to add to this object.
     */
    public void add( SoundResource soundResource ) {
    	model.add(soundResource, resolution);
    }
    
    /**
     * Adds the given soundResources to this model. Only add if this object does 
     * not already have the given soundResource.
     * 
     * @param soundResources to add to this object.
     */
    public void add( List<SoundResource> soundResources ) {
    	model.add(soundResources, resolution);
    }
    
    /**
     * Remove the given soundResource from this model.
     * 
     * @param soundResource to remove
     */
    public void remove( SoundResource soundResource ) {
    	model.remove(soundResource);
    }
    
    /**
     * Remove the given list of soundResources from this model.
     * 
     * @param removed SoundResources
     */
    public void remove( List<SoundResource> removed ) {
    	model.remove(removed);
    }
    
    /**
     * Replace the elements of selected with elements from the given List
     * newSelected.
     * 
     * @param newSelected List of soundResources whose elements replace selected
     * 					  elements
     */
    public void setSelected( List<SoundResource> newSelected ) {
    	model.setSelected(newSelected);
    }
    
    /**
     * Compare the given List<soundResource> to the record of the last selection
     * inside this class.
     *
     * @param currentSelected The List of soundResources to compare with the 
     * 						  previous record.
     *
     * @return True if both arrays contain the same elements.
     */
    public boolean compareLastSelected( List<SoundResource> currentSelected) {
        boolean isEqual = false;

        // if two sets are the same size and contain the same elements, set
        // isEqual to true
        if( currentSelected.size() == currentLastSelected.size() &&
                currentSelected.containsAll( currentLastSelected ) ){

            isEqual = true;
        }

        return isEqual;
    }

    /**
     * Update the record of the last selection with the currently selected.
     */
    public void updateLastSelected() {
        currentLastSelected = new ArrayList<SoundResource>(model.getSelected());
    }
    
    /**
     * Update the given soundResource's location in the panel.
     * 
     * @param soundResource to update location for
     */
    public void updateLocation( SoundResource soundResource ) {
    	model.updateLocation(soundResource, resolution);
    }
    
    /**
     * Update the given soundResource's location in the panel.
     * 
     * @param soundResources to update location for
     */
    public void updateLocation( List<SoundResource> soundResources ) {
    	model.updateLocation(soundResources, resolution);
    }
    
    /**
     * Update the snap to guides in the model's view. Forces the view to hide
     * and show guides as necessary.
     * 
     * If the given guideDelay would cause the distance between guidelines to
     * be smaller than MIN_GUIDE_DELAY, set distance to that constant.
     */
    public void updateSnapToGuides() {
    	
    	// calculate distance between guides relative to resolution
    	int guideDistance = guideDelay / resolution;
    	
    	if( guideDistance < Constants.MIN_GUIDE_DISTANCE ) {
            guideDistance = Constants.MIN_GUIDE_DISTANCE;
    	}
    	
    	// get the model to update its views
    	model.updateSnapToGuides( isSnapToEnabled, guideDistance );
    }
    
    /**
     * When given a new resolution, update all components of this model's view
     * accordingly.
     * 
     * @param r int resolution (width = sound.length / resolution)
     */
    public void setResolution( int r ) {
    	// Set the new resolution and tell the model to notify the panel
    	resolution = r;
    	
    	model.updateLocation( model.getSoundResources(), r );
    }
    
    /**
     * When given a Point pixel location, convert it to an absolute location.
     * 
     * @param pixelLocation Point pixel location to convert
     * @return Point absolute location
     */
    public Point calculateAbsoluteFromPixel( Point pixelLocation ) {
    	
    	// absolute position = pixel position * resolution
    	Point absoluteLocation = new Point( 
    			(int)( pixelLocation.getX() * resolution ),
    			(int)( pixelLocation.getY() ) );
    	
    	return absoluteLocation;
    }
    
    /**
     * Clear all soundResources, selected, edits, etc. to restore this model to
     * a like new state.
     */
    public void clearAllChanges() {
    	
    	currentLastSelected.clear();	
    	model.clearAllChanges();

    	undoManager.discardAllEdits();
    }
    
    /** 
     * Selects given SoundResource.
     * Doesn't add the SoundResource if it's already selected.
     * 
     * @param soundResource SoundResource to select.
     */
    public void selectSoundResource(SoundResource soundResource) {
    	model.selectSoundResource(soundResource);
    }

    /**
     * Remove a SoundResource from the model's selected.
     * 
     * @param soundResource SoundResource to remove.
     */
    public void deselectSoundResource(SoundResource soundResource) {
    	model.deselectSoundResource(soundResource);
    }
	
    /** 
     * Select all the SoundResources and add this edit to the undo manager. 
     */
    public void selectAll() {
    	
    	// Update the static record in the select edit because it won't be
    	// updated in the right place otherwise.
    	updateLastSelected();
    	
    	// select all soundResources
        for (SoundResource soundResource : model.getSoundResources() ) {
            model.selectSoundResource( soundResource );
        }
        
        // add this edit to the undo manager if it changes selected
        // containsAll works because the old selected will never be larger than
        // the new selected.
        if( ! currentLastSelected.containsAll( model.getSelected() ) ) {
            undoManager.addEdit( new SelectEdit(this) );
        }
    }
    
    /** 
     * Delete all elements of selected from the model while adding
     * this edit to the undo manager.
     */
    public void deleteSelected() {
    	// create and add this edit to the UndoManager
    	undoManager.addEdit( new DeleteEdit(this) );
    	
    	deleteSelectedWithoutUndo();
    }
    
    /** 
     * Delete all elements of selected from the model without
     * adding this edit to the undo manager.
     */
    public void deleteSelectedWithoutUndo() {
    	model.deleteSelectedWithoutUndo();
    }
    
    /** 
     * Deselect all selected.
     */
    public void clearSelection() {
    	model.clearSelection();
    }
    
    /**
     * Play the model's composition.
     */
    public void play() {
    	model.play();
    }
    
    /**
     * Return the length of the model's composition in ms.
     * 
     * @return length in ms of playing time
     */
    public int duration() {
    	return model.duration();
    }
    
    /**
     * When given the number of repeats and a list of soundResources, repeat
     * soundResources repeat times and put the beginning of each successive 
     * repeat on the end of the last repeat. Is an undoable action.
     * 
     * @param repeat int times to repeat
     * @param soundResources List<SoundResources> to repeat
     * @param space to put at the end of the total repeated sound
     */
    public void repeatSounds( int repeat, List<SoundResource> soundResources, 
    	int space) {
    	
        // make a new list of soundResources to hold the repeats
        List<SoundResource> repeats = new ArrayList<SoundResource>();

        // Keep the duration of the entire repeat
        int entireDuration = model.duration(soundResources);

    	// for each repeat...
    	for( int i=1; i<=repeat; i++ ) {

            // for each sound...
            for( SoundResource sound : soundResources ) {
                // find the new sound's delay
                int delay = sound.getDelay() + i * (entireDuration + space);

                // make the new repeat sound
                SoundResource newSound = new SoundResource(
                    (SoundResource)(sound.getSound()),
                    delay,
                    sound.getTrack() );

                // add the repeat sound to repeats
                repeats.add(newSound);
            }
    	}
    	
    	// add the finished result in an undoable way
    	addWithEdit(repeats);
    }
    
    /**
     * Add the given SoundResources to the model, select them all, and add the
     * appropriate edit to the UndoManager. Sort of a convenience method.
     * 
     * @param soundResources to add to the model
     */
    public void addWithEdit( List<SoundResource> soundResources ) {
        // add and select the pasted soundResources
        add(soundResources);
        clearSelection();
        for( SoundResource soundResource : soundResources ) {
            selectSoundResource(soundResource);
        }
		
        // Add proper undo changes
        addEdit( new AddEdit(this, soundResources) );
        addEdit( new SelectEdit(this, false, true) );
    }
    
    /**
     * Organize the model's sounds so that the same sounds line up on the same 
     * y-values.
     */
    public void organizeSounds() {
    	model.organizeSounds(resolution);
    }
}