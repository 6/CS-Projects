/*
 * File: SelectEdit.java
 * Authors: Martha Witick, Peter Graham
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
 * Allows a new selection on the CompositionModel to be undone and then redone.
 * This object knows the CompositionModel where its change takes place and
 * the changes in the List of selected objects that this edit causes.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class SelectEdit implements UndoableEdit
{
    /** Know the model controller to keep track of selections in. */
    private CompositionModelController modelController;

    /** Know what is selected after this edit. */
    private List<SoundResource> selected;

    /** Know what was previously selected before this edit. */
    private List<SoundResource> previouslySelected;

    /**
     * Know if this edit is significant or not. Insignificant edits do not stop
     * the undo, so many of them can be done at once.
     */
    private boolean isSignificant;
    
    /** Set to true if isSignificant needs to be flipped every undo/redo. */
    private boolean swapSignificant;

    /** Keep track if undone has been called more recently than redo. */
    private boolean isUndone;


    /**
     * Constructor: initialize and set fields. Set isSignificant to true.
     *
     * @param cmc Set the Composition Model Controller to watch for selection.
     */
    public SelectEdit( CompositionModelController cmc ) {
        this( cmc, true ); 
    }

    /**
     * Constructor: initialize and set fields. Does not allow swapping
     *              significance.
     *
     * @param cmc Set the Composition Model Controller to watch for selection.
     * @param significant Sets isSignificant. Set to false for the undo manager
     *                    to do this edit and the next significant edit.
     */
    public SelectEdit( CompositionModelController cmc, boolean significant ) {
        this( cmc, significant, false );
    }

    /**
     * Constructor: initialize and set fields.
     *
     * @param cmc Set the Composition Model Controller to watch for selection.
     * @param significant Sets isSignificant. Set to false for the undo manager
     *                    to do this edit and the next significant edit.
     * @param swap Set swap to true if significance needs to be swapped every
     * 			   undo/redo.
     */
    public SelectEdit( CompositionModelController cmc, boolean significant, 
            boolean swap) {
        modelController = cmc;
        selected = new ArrayList<SoundResource>( modelController.getSelected());
        previouslySelected = new ArrayList<SoundResource>(
        		modelController.getCurrentLastSelected());
        modelController.updateLastSelected();
        isSignificant = significant;
        swapSignificant = swap;

        isUndone = false;
    }

    /**
     * Method for aggregating UndoableEdits not supported for SelectEdits.
     *
     * @return - false
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
     * Retrieve a human readable description of the SelectEdit.
     * 
     * @return - human readable description of the SelectEdit.
     */
    public String getPresentationName()
    {
        return "SelectionEdit";
    }

    /**
     * Retrieves a human readable description of redo action.
     *
     * @return - a human readable description of redo action.
     */
    public String getRedoPresentationName()
    {
        return "Redo Selection Change";
    }
    
    /**
     * Retrieves a human readable description of undo action.
     *
     * @return - a human readable description of undo action.
     */
    public String getUndoPresentationName()
    {
        return "Undo Selection Change";
    }

    /**
     * Determines if the edit is considered significant.
     * 
     * @return - whether this edit is significant
     */
    public boolean isSignificant()
    {
        return isSignificant;
    }

    /**
     * Sets the CompositionModel's selection to the newer selected list
     * of SoundResources.
     * 
     * @throws CannotRedoExpection when the method fails somehow.
     */
    public void redo() throws CannotRedoException
    {
    	
        if(this.isUndone) {
            modelController.setSelected(new ArrayList<SoundResource>(selected));
        }
        else
            throw new CannotRedoException();
        
        // if swapSignificant is true, swap isSignificant
        if( swapSignificant ) {
            isSignificant = !isSignificant;
        }
        
        this.isUndone = false;
    }

    /**
     * If an edit cannot be added to the UndoableEdit should the
     * edit-to-be-added replace the pre-existing one.
     *
     * @param anEdit - the edit that would replace this edit
     * @return - false, SelectEdits are not to be replaced
     */
    public boolean replaceEdit(UndoableEdit anEdit)
    {
        return false;
    }

    /**
     * Sets the CompositionModel's selection to the previously selected list
     * of SoundResources.
     * 
     * @throws CannotRedoExpection when the method fails somehow.
     */
    public void undo() throws CannotUndoException
    {	
    	if(!this.isUndone) {
            modelController.setSelected(
                    new ArrayList<SoundResource>(previouslySelected));
    	}
    	else
            throw new CannotRedoException();
    	
    	// if swapSignificant is true, swap isSignificant
        if( swapSignificant ) {
            isSignificant = !isSignificant;
        }
    	
    	this.isUndone = true;
    }
}