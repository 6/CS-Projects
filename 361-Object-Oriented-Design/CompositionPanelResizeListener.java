/*
 * File: CompositionPanelResizeListener.java
 * Authors: Martha Witick, Peter Graham
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 10 December
 */
package project8;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Detects when panel is resized. Meant to be used for CompositionPanel.
 * 
 * @author mmwitick
 * @author pagraham
 */
public class CompositionPanelResizeListener implements ComponentListener{

    /** the model changes are made on */
    private CompositionModelController modelController;
    
    /**
     * Construct this object and initialize fields.
     * 
     * @param cmc The model controller whose SoundResources can be selected.
     */
    public CompositionPanelResizeListener(CompositionModelController cmc) {
        modelController = cmc;
    }

    /**
     * Makes changes to scrollbar and guide lines if necessary.
     * 
     * @param event - ComponentEvent that triggers this method.
     */
    public void componentResized(ComponentEvent event) {
        modelController.updateSnapToGuides();
    }

    // unused interface methods
    public void componentHidden(ComponentEvent e) { }
    public void componentShown(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }

}