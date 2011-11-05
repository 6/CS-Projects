/*
 * File: MoveEdit.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Will O'Brien
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 6 November
 */
package project8; 

import java.awt.Point;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Implement an UndoableEdit applied to ImageSound location changes/moves inside
 * a CompositionModel. Knows the model to apply this edit and knows the 
 * difference between final position after the change and initial position
 * before the change.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class MoveEdit implements UndoableEdit
{
    /** know the model to change (necessary to know selected) */
    private CompositionModelController modelController;

    /** know absolute offset between final and original mouse move */
    private Point offset;

    /** Keep track of whether or not undo has been called most recently */
    private boolean isUndone;

    /**
     * Initialize fields and set the associated CompositionModel and Point
     * offset.
     * @param cmc CompositionModel controller to keep track of moves on.
     * @param o Offset (end - start) to move SoundResources in panel. Absolute 
     * 		offset, adjustments for resolution are calculated within the edit.
     */
    public MoveEdit( CompositionModelController cmc, Point o ) {
        modelController = cmc;
        offset = o;
        this.isUndone = false;
    }

    /**
     * Method to aggregate this UndoableEdit with another. MoveEdit
     * does not allow aggregation.
     *
     * @return - false, MoveEdit does not allow undoable actions to
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
        // No Cleanup Required
    }

    /**
     * Retrieve a human readable description of the MoveEdit.
     * 
     * @return - human readable description of the MoveEdit.
     */
    public String getPresentationName()
    {
        return "MoveEdit";
    }

    /**
     * Retrieves a human readable description of redo action.
     *
     * @return - a human readable description of redo action.
     */
    public String getRedoPresentationName()
    {
        return "Redo Move";
    }

    /**
     * Retrieves a human readable description of undo action.
     *
     * @return - a human readable description of undo action.
     */
    public String getUndoPresentationName()
    {
        return "Undo Move";
    }

    /**
     * Determines if the edit is considered significant.
     * 
     * @return - true, moving SoundResources is always significant
     */
    public boolean isSignificant()
    {
        return true;
    }
    
    /**
     * Sets the location of all currently selected components to (their current
     * location + the offset of the move)
     * 
     * @throws CannotRedoException if cannot be redone
     */
    public void redo() throws CannotRedoException
    {
    	if(this.isUndone)
    	{
            // Set location of each selected SoundResource to current + offset
            for( SoundResource si : modelController.getSelected() ){
            	si.setLocation( (int) (si.getDelay() + offset.getX()),
                                (int) (si.getTrack() + offset.getY()) );
            	modelController.updateLocation(si);
            }
    	}
    	else
            throw new CannotRedoException();
    	
        this.isUndone = false;
    	
    }


    /**
     * If an edit cannot be added to the UndoableEdit should the
     * edit-to-be-added replace the preexisting one.
     *
     * @param anEdit - the edit that would replace this edit
     * @return - false, MoveEdits are not to be replaced
     */
    public boolean replaceEdit(UndoableEdit anEdit)
    {
        return false;
    }
    
    
    /**
     * Sets the location of all currently selected components to (their current
     * location - the offset of the move)
     * 
     * @throws CannotUndoException if cannot be undone
     */
    public void undo() throws CannotUndoException
    {
    	if(!this.isUndone)
    	{
            // Set location of each selected SoundResource to current - offset
            for( SoundResource si : modelController.getSelected() ){
            	si.setLocation( (int) (si.getDelay() - offset.getX() ),
                                (int) (si.getTrack() - offset.getY()) );
            	modelController.updateLocation(si);
            }
    	}
        else
            throw new CannotRedoException();
    	
        this.isUndone = true;
    }
}