/*
 * File: DeleteEdit.java
 * Author: Martha Witick, Peter Graham
 * Previous Authors: Will O'Brien
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 6 November
 */
package project8;

import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Undoable edit that allows a delete action to be reversed in the undo method
 * and to remove again the elements that had previously been restored.
 * It knows the CompositionModel where its change takes place.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class DeleteEdit implements UndoableEdit
{
    /** Know the controller of the model to change */
    private CompositionModelController modelController;

    /** Know the SoundResources to delete/undelete */
    private List<SoundResource> deleted;

    /** Keep track of whether or not undo has been called most recently */
    private boolean isUndone;

    /**
     * Initialize fields.
     *
     * @param cmc CompositionModelController to associate this Edit with.
     */
    public DeleteEdit( CompositionModelController cmc ) {
        modelController = cmc;
        deleted = new ArrayList<SoundResource>( modelController.getSelected() );
        isUndone = false;
    }
	
    /**
     * Method to aggregate this UndoableEdit with another. DeleteEdit
     * does not allow aggregation.
     *
     * @return - false, DeleteEdit does not allow undoable actions to
     * be aggregated.
     */
    public boolean addEdit(UndoableEdit anEdit)
    {
        return false;
    }

    /**
     * Method that determines if redo can be executed without throwing a
     * CannotRedoException.
     * 
     * @return - returns true if undo has been called more recently than redo
     */
    public boolean canRedo()
    {
        return isUndone;
    }

    /**
     * Method that determines if undo can be executed without throwing a
     * CannotUndoException.
     * 
     * @return - returns true if undo has not yet been called or if redo has
     * been called more recently than undo.
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
     * Retrieve a human readable description of the DeleteEdit.
     * 
     * @return - human readable description of the DeleteEdit.
     */
    public String getPresentationName()
    {
        return "DeleteEdit";
    }

    /**
     * Retrieves a human readable description of redo action.
     *
     * @return - a human readable description of redo action.
     */
    public String getRedoPresentationName()
    {
        return "Redo delete";
    }

    /**
     * Retrieves a human readable description of undo action.
     *
     * @return - a human readable description of undo action.
     */
    public String getUndoPresentationName()
    {
        return "Undo delete";
    }

    /**
     * Determines if the edit is considered significant.
     * 
     * @return - true, deleting SoundResources is always significant
     */
    public boolean isSignificant()
    {
        return true;
    }

    /**
     * Delete selected listeners in the InteractiveJPanel
     * 
     * @throws CannotRedoException
     */
    public void redo() throws CannotRedoException
    {   	
        modelController.deleteSelectedWithoutUndo();
        
        this.isUndone = false;
    }

    /**
     * If an edit cannot be added to the UndoableEdit should the
     * edit-to-be-added replace the pre-existing one.
     *
     * @param anEdit - the edit that would replace this edit
     * @return - false, DeleteEdit s are not to be replaced
     */
    public boolean replaceEdit(UndoableEdit anEdit)
    {
        return false;
    }

    /**
     * Adds the previously deleted elements back to the CompositionModel and
     * selects them.
     * 
     * @throws CannotUndoException
     */
    public void undo() throws CannotUndoException
    {
    	// For each SoundResource in deleted, add it back to model and select
        for(SoundResource soundResource : deleted) {
            modelController.add(soundResource);
            modelController.selectSoundResource(soundResource);
        }
        
        this.isUndone = true;
    }
}