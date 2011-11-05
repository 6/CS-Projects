/*
 * File: FileLoadListener.java
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
import java.util.List;

import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

/**
 * Listens for 'Open' event, which allows the user to select and open a file.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class FileLoadListener implements ActionListener {
	
    /** CompositionModelController to load changes into */
    private CompositionModelController modelController;

    /** FileHandler to load files with */
    private FileHandler fileHandler;

    /** Dialog to get the desired file to open with */
    private FileDialog dialog;

    /** Frame to show errors on */
    private Frame frame;

    /** Control Panel to update changes on */
    private JControlPanel controlPanel;

    /**
     * Initialize fields.
     *
     * @param f Frame to open the file dialog over
     * @param m Model to load changes into
     * @param cp Control Panel to update
     * @param fh FileHandler to interpret files with
     */
    public FileLoadListener( Frame f, CompositionModelController cmc,
            JControlPanel cp, FileHandler fh) {
        frame = f;
        modelController = cmc;
        fileHandler = fh;
        controlPanel = cp;
        dialog = new FileDialog(f, "Open Composition");
    }
	
    /**
     * When called, open a dialog box and load the selected file into the model.
     * 
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {

        // Allow the user to choose a file
        dialog.setVisible(true);

        // Only load if the filename is not null
        String filename = dialog.getDirectory() + dialog.getFile();
        if( dialog.getFile() != null ) {

            try {
                // Remove old changes
                modelController.clearAllChanges();
                controlPanel.clearUserDefinedGestures();

                // Load the List<SoundResource> into the model and update
                List<SoundResource> soundResources = fileHandler.loadFromFile(
                    filename);
                modelController.add(soundResources);

            } catch (SAXException e) {
                JOptionPane.showMessageDialog(frame,
                                "Load failed. "+e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                                "Load failed. "+e.getMessage());
                e.printStackTrace();
            }
    	}
    }
}