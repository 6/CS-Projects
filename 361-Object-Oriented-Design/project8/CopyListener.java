/*
 * File:    CopyListener.java
 * Author:  Martha Witick, Peter Graham
 * Class:   CS 360, Fall 2010
 * Project: 8
 * Date:    22 November
 */
package project8;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

/**
 * Copy the selected items in the composition panel and add them to the system 
 * clipboard.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class CopyListener implements ActionListener {
	
    /** The model to copy items from. */
    private CompositionModelController modelController;
	
    /** the file handler for handling XML conversions */
    private FileHandler handler;
    
    /** frame to show messages on */
    private Frame frame;

    /**
     * Constructor that takes in a CompositionModel and sets this object's
     * panel to copy items from.
     * 
     * @param f Frame to open the error dialog over
     * @param cmc CompositionModel controller to copy items from.
     * @param fh the file handler for handling XML conversions
     */
    public CopyListener(Frame f, CompositionModelController cmc, 
    		FileHandler fh) {
    	frame = f;
        modelController = cmc;
        handler = fh;
    }
    
    /**
     * Copy selected items in composition panel and add them to the system
     * clipboard.
     *
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
        try {
            // get selected items
            String selectedXml = handler.saveToString(
                modelController.getSelected());

            // add them to the system clipboard
            StringSelection selection = new StringSelection(selectedXml);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipBoard = toolkit.getSystemClipboard();
            clipBoard.setContents(selection, selection);

        } catch (TransformerException e) {
            JOptionPane.showMessageDialog(frame,
                                "Copy failed. "+e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                                "Copy failed. "+e.getMessage());
        }
    }
}