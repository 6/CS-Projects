/*
 * File: UndoActionListener.java
 * Authors:  Martha Witick, Peter Graham
 * Previous Authors: Will O'Brien
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 27 October
 */
package project8;

import java.awt.event.ActionEvent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionListener;

/**
 * Listens for ActionEvents and calls UndoManager's undo method.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class UndoActionListener implements ActionListener
{
    /** UndoManager to call undo() on. */
    private UndoManager undoManager;

    /**
     * Initializes fields.
     * 
     * @param u This object's UndoManager to call undo on.
     */
    public UndoActionListener(UndoManager u)
    {
        undoManager = u;
    }

    /**
     * When called, call undo on its UndoManager
     * 
     * @param e ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent e)
    {
        try {
            if(undoManager.canUndo())
                undoManager.undo();
        } catch (CannotUndoException ex) {
            System.out.println("Unable to undo: " + ex);
            ex.printStackTrace();
        }
    }
}