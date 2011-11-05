/*
 * File: RedoActionListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Will O'Brien
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 6 November
 */
package project8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * Listens for ActionEvents and calls UndoManager's redo method.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class RedoActionListener implements ActionListener
{
    /** The UndoManager to call redo on. */
    private UndoManager undoManager;

    /**
     * Construct this object and hand it an UndoManager to call redo() on.
     * 
     * @param u UndoManager to call redo on.
     */
    public RedoActionListener(UndoManager u)
    {
        undoManager = u;
    }

    /**
     * When called, call redo on its UndoManager
     * 
     * @param e ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent e)
    {
        try {
            if(undoManager.canRedo())
                undoManager.redo();
        }

        catch (CannotUndoException ex) {
            System.out.println("Unable to redo: " + ex);
            ex.printStackTrace();
        }
    }
}