/*
 * File:    CutListener.java
 * Author:  Martha Witick, Peter Graham
 * Class:   CS 360, Fall 2010
 * Project: 8
 * Date:    13 November
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
 * Cut the selected items in the composition panel and add them to the system 
 * clipboard.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class CutListener implements ActionListener {

    /** The model to cut items from. */
    private CompositionModelController modelController;
    
    /** the file handler for handling XML conversions */
    private FileHandler handler;
    
    /** frame to show messages on */
    private Frame frame;

    /**
     * Constructor that takes in a CompositionModel and sets this object's
     * panel to cut items from.
     * 
     * @param f Frame to open the error dialog over
     * @param m CompositionModel to cut items from.
     * @param fh the file handler for handling XML conversions
     */
    public CutListener(Frame f, CompositionModelController cmc, FileHandler fh){
    	frame = f;
        modelController = cmc;
        handler = fh;
    }
	
    /**
     * Cut selected items in composition panel and add them to system clipboard.
     *
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
        // get selected items and delete them from panel
        try {
                String selectedXml = handler.saveToString(
                        modelController.getSelected() );

                modelController.deleteSelected();

                // add them to the system clipboard
                StringSelection selection = new StringSelection(selectedXml);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipBoard = toolkit.getSystemClipboard();
                clipBoard.setContents(selection, selection);

        } catch (TransformerException e) {
            JOptionPane.showMessageDialog(frame,
                                "Cut failed. "+e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                                "Cut failed. "+e.getMessage());
        }
    }
}