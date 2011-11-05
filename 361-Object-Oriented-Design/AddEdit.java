/*
 * File: AddEdit.java
 * Author: Martha Witick, Peter Graham
 * Previous Authors: Will O'Brien
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 10 December
 */
package project8;

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Implements an UndoableEdit for adding SoundResources to a CompositionModel
 * Allows the addition to be undone and then redone. Knows the 
 * CompositionModel to edit and the SoundResource to add/delete.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class AddEdit implements UndoableEdit {
    
    /** Know the panel this edits */
    private CompositionModelController modelController;

    /** Know the added SoundResource */
    private List<SoundResource> added;

    /** Know if the undo method has been called last */
    private boolean isUndone;

    /** Know if we're significant, this swaps itself on undo/redo */
    private boolean isSignificant;
    
    /**
     * Create a new AddEdit for the component passed on the
     * CompositionModel.
     *
     * @param cmc - the CompositionModelController SoundResource was added to
     * @param soundResource - the SoundResource that was added to the model
     */
    public AddEdit( CompositionModelController cmc, SoundResource soundResource) 
    {
        modelController = cmc;
        added = new ArrayList<SoundResource>();
        added.add(soundResource);
        
        isUndone = false;
        isSignificant = true; // start as significant
    }
    
    /**
     * Create a new AddEdit for the given SoundResources added to the model.
     * This constructor keeps track of multiple adds at once.
     *
     * @param cmc - CompositionModelController the SoundResources were added to
     * @param soundResources - SoundResources that were added to the model
     */
    public AddEdit( CompositionModelController cmc, 
            List<SoundResource> soundResources ) {
        modelController = cmc;
        added = soundResources;
        isUndone = false;
        isSignificant = true; // start as significant
    }

    /**
     * Tests whether an additional UndoableEdit can be added to this
     * UndoableEdit for aggregation purposes. Always false for this class
     * because this class cannot be aggregated.
     *
     * @param anEdit - The edit that will not be added
     * @return - false, this class does allow aggregation
     */
    public boolean addEdit(UndoableEdit anEdit)
    {
        return false;
    }

    /**
     * Determine if the redo method can be called successfully.
     *
     * @return - false unless the undo method has been called more recently
     * than the redo method.
     */
    public boolean canRedo()
    {
        return isUndone;
    }

    /**
     * Determine if the undo method can be called successfully.
     *
     * @return - true if the undo method has not yet been called or if redo was
     * called more recently than undo.
     */
    public boolean canUndo()
    {
    	return !isUndone;
    }

    /**
     * Performs all necessary cleanup operations; prepares for class to be
     * dereferenced.
     */
    public void die()
    {
        // No cleanup required
    }

    /**
     * Retrieve a human readable description of the AddEdit.
     * 
     * @return - human readable description of the AddEdit.
     */
    public String getPresentationName()
    {
        return "AddEdit";
    }

    /**
     * Retrieves a human readable description of redo action.
     *
     * @return - a human readable description of redo action.
     */
    public String getRedoPresentationName()
    {
        return "Redo Add Component";
    }

    /**
     * Retrieves a human readable description of undo action.
     *
     * @return - a human readable description of undo action.
     */
    public String getUndoPresentationName()
    {
        return "Undo Add Component";
    }

    /**
     * Determines if the edit is considered significant.
     * 
     * @return - if the edit is significant or not
     */
    public boolean isSignificant()
    {
        return isSignificant;
    }


    /**
     * Adds the SoundResource back to the CompositionModel and selects it.
     * 
     * @throws CannotRedoException when the method cannot be called.
     */
    public void redo() throws CannotRedoException
    {  	
        modelController.add( added );
        
        isSignificant = !isSignificant;
        this.isUndone = false;
    }

    /**
     * If an edit cannot be added to the UndoableEdit should the
     * edit-to-be-added replace the preexisting one.
     *
     * @param anEdit - the edit that would replace this edit
     * @return false, AddEdits are not to be replaced
     */
    public boolean replaceEdit(UndoableEdit anEdit)
    {
        return false;
    }

    /**
     * Deselects and removes the added SoundResource from the panel.
     * 
     * @throws CannotUndoException when the method cannot be called.
     */
    public void undo() throws CannotUndoException
    {
        modelController.remove( added );
        
        isSignificant = !isSignificant;
        this.isUndone = true;
    }
}