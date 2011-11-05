/*
 * File: FileSaveListener.java
 * Listener.java TODO check this name matches
 * Authors:  Martha Witick, Peter Graham
 * Class: CS 361, Fall 2010
 * Project 8
 * Date: 12 November
 */
package project8;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

/**
 * Listens for a save event, which allows the user to select and save a file.
 * .
 * @author Martha Witick
 * @author Peter Graham
 */
public class FileSaveListener implements ActionListener {
	
    /** CompositionModel to load changes from */
    private CompositionModelController modelController;

    /** FileHandler to load files with */
    private FileHandler fileHandler;

    /** Dialog to get the desired file to save with */
    private FileDialog dialog;
	
    /** frame to show messages on */
    private Frame frame;
	
    /**
     * Initialize fields.
     *
     * @param f Frame to open the file dialog over
     * @param cmc Model controller to load changes from
     * @param fh FileHandler to interpret files with
     */
    public FileSaveListener(Frame f, CompositionModelController cmc,
            FileHandler fh ) {
        frame = f;
        modelController = cmc;
        fileHandler = fh;
        dialog = new FileDialog(frame, "Save Composition", FileDialog.SAVE);
    }
	
    /**
     * When called, open a dialog box and save the model's current state into
     * the user's selected file.
     * 
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
    	
    	// Allow the user to choose a file
    	dialog.setVisible(true);
    	
    	// Only save if the filename is not null
    	String filename = dialog.getDirectory() + dialog.getFile();
    	if( dialog.getFile() != null ) {

            // Load the List<SoundResource> from the model and save
            try {
                fileHandler.saveToFile( filename,
                                modelController.getSoundResources() );
            } catch (TransformerException e) {
                JOptionPane.showMessageDialog(frame,
                                    "Save failed. "+e.getMessage());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                                    "Save failed. "+e.getMessage());
            }
    	}
    }
}