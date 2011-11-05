/*
 * File:    PasteListener.java
 * Author:  Martha Witick, Peter Graham
 * Class:   CS 360, Fall 2010
 * Project: 8
 * Date:    13 November
 */
package project8;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

/**
 * Paste items in the system clipboard into the composition panel. 
 * 
 * @author mmwitick
 * @author pagraham
 */
public class PasteListener implements ActionListener {

    /** The model controller to paste items into. */
    private CompositionModelController modelController;
    
    /** the file handler for handling XML conversions */
    private FileHandler handler;
    
    /** frame to show messages on */
    private Frame frame;

    /**
     * Constructor that takes in a CompositionModel and sets this object's
     * panel to paste items into.
     * 
     * @param f Frame to open the error dialog over
     * @param cmc CompositionModel controller to paste items into.
     * @param fh the file handler for handling XML conversions
     */
    public PasteListener(Frame f, CompositionModelController cmc, 
            FileHandler fh) {
    	frame = f;
        modelController = cmc;
        handler = fh;
    }
	
    /**
     * Paste items in the system clipboard into the composition panel.
     *
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
        // get the contents of the clipboard
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipBoard = toolkit.getSystemClipboard();
        Transferable contents = clipBoard.getContents(null);
        String xmlStr = null;
        if(contents != null) {
            contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            try {
                xmlStr = (String) contents.getTransferData(
                                                DataFlavor.stringFlavor);

            } catch (UnsupportedFlavorException ufe) {
                JOptionPane.showMessageDialog(frame,
                                "Paste failed. "+ufe.getMessage());
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(frame,
                                "Paste failed. "+ioe.getMessage());
            } catch (NullPointerException npe) {
                JOptionPane.showMessageDialog(frame,
                                "Paste failed. "+npe.getMessage());
            }
        }

        // put the contents into the model
        try {
            List<SoundResource> soundResources = handler.loadFromString(xmlStr);

            modelController.addWithEdit(soundResources);

        } catch (SAXException e) {
            JOptionPane.showMessageDialog(frame,
                            "Paste failed. "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                            "Paste failed. "+e.getMessage());
            e.printStackTrace();
        }
    }
}