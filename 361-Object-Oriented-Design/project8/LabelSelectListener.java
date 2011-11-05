/*
 * File: LabelSelectListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Leah Perlmutter, Katherine Smith, Will O'Brien
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Listens for mouse movement, mouse clicks, and actions to control
 * which SoundResources in this Composition are selected. Use mouse clicks
 * and a selecting rubber band selectionBox determined by mouse click/location
 * on the attached panel.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class LabelSelectListener 
        implements MouseMotionListener, MouseListener, ActionListener {

    /** the model changes are made on */
    private CompositionModelController modelController;
    
    /** the selection box for selecting multiple labels */
    private JLabel selectionBox;

    /**
     *  Components selected before the selectionBox is drawn during a control 
     *  click.
     */
    private List<SoundResource> previousSelection;

    /** x coordinate of last mouse press */
    private int clickX;
    
    /** y coordinate of last mouse press */
    private int clickY;
    
    /**
     * Construct this object and initialize fields.
     * 
     * @param cmc The model controller whose SoundResources can be selected.
     */
    public LabelSelectListener(CompositionModelController cmc) {
        modelController = cmc;
        selectionBox = new JLabel();
        selectionBox.setBorder( new LineBorder(Color.BLACK,1) );
        previousSelection = new ArrayList<SoundResource>();
    }

    /**
     * Make the selection box visible on the panel or select an icon depending
     * on the component at MouseEvent me's location.
     * 
     * @param me The MouseEvent that triggers this method.
     */
    public void mousePressed(MouseEvent me) {

    	// Get the panel this listener listens for clicks on
    	CompositionPanel panel = (CompositionPanel)(me.getSource());
    	
        JComponent clickedThing =
                (JComponent) panel.getComponentAt(me.getX(), me.getY());
        
        SoundResource soundResource = panel.getSoundResourceFromComponent(
            clickedThing);
        

        // if user clicked background
        if(clickedThing == panel ) {

            // preserve current selected when control clicked
            if(me.isMetaDown() ) { // control click
                previousSelection.addAll( modelController.getSelected() );
            }

            // manipulate the selection box
            modelController.setSelectionMode(true);
            initSelectionBox(me);
        }
        // or if user clicked an icon
        else {
            // do not manipulate the selection box
            modelController.setSelectionMode(false);

            // check and see if the icon is selected
            LineBorder border = ((LineBorder)(clickedThing.getBorder()));
            boolean isSelected = ( border != null && 
                border.getLineColor() == Color.yellow );
            
            // toggle whether the clickedThing is selected on control click
            if (me.isMetaDown() && soundResource != null) {
                toggleSelected(soundResource);
            }

            // On normal click, select only the clickedThing and add this edit
            // to the undo manager.
            else {
                if (!isSelected && soundResource != null) {
                    selectOnlyThis(soundResource);
                }
            }
        }
    }
    
    /**
     * Resize and refresh the selection box on drag.
     * 
     * @param me MouseEvent whose location resizes the bounds of the selection
     *           box.
     */
    // a different listener handles drag events that move labels
    public void mouseDragged(MouseEvent me) {

        if(modelController.isSelecting()) {
        	// selection box is visible on panel
            resizeSelectionBox(me);
            refreshSelectedList(me);
        }
    }
    
    /**
     * Clean up from the selection box and save any undoable changes in an edit.
     * 
     * @param me MouseEvent that triggers this method.
     */
    public void mouseReleased(MouseEvent me) {
    	
    	// get the panel the listener listens on
    	JPanel panel = (JPanel)(me.getSource());
    	
    	// if selection has been changed with the selection box,
    	// add the edit to the undo manager
    	if( modelController.isSelecting() &&
            !modelController.compareLastSelected(modelController.getSelected()))
    	{
            // add the edit to the undo manager
            modelController.addEdit( new SelectEdit( modelController ) );
    	}
    	
        // resize the selectionBox to zero
        selectionBox.setBounds(0,0,0,0);

        // remove selectionBox from the panel and update the panel
        panel.remove( selectionBox );
        panel.updateUI();

        // clear saved previously selected components
        previousSelection.clear();
    }

    /**
     * On action, select all labels in this object's associated 
     * CompositionModel.
     * 
     * @param ae ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent ae) {
        modelController.selectAll();
    }
    
    /**
     * Save the initial press so that selection box can update as the mouse
     * drags, and adds the selectionBox to the panel
     * 
     * @param me MouseEvent that triggers this method.
     */
    private void initSelectionBox(MouseEvent me) {
    	JPanel panel = (JPanel)(me.getSource());
    	
        // set the coordinates of this click
        clickX = me.getX();
        clickY = me.getY();

        // add the selection box to the panel and update the panel
        panel.add(selectionBox);
        panel.updateUI();
    }
    
    /**
     * Deselect all components, select given component, add edit to undo
     * 
     * @param soundResource - SoundResource that was clicked
     */
    private void selectOnlyThis(SoundResource soundResource) {
        modelController.clearSelection();
        modelController.selectSoundResource(soundResource);
        modelController.addEdit( new SelectEdit(modelController) );
    }

    /**
     * Adds or removes a SoundResource from the selection, add edit to undo
     * 
     * @param soundResource - component that was clicked
     */
    private void toggleSelected(SoundResource soundResource) {
        if (modelController.isSelected(soundResource)) {
            modelController.deselectSoundResource(soundResource);
        } else { // if clickedThing not selected
            modelController.selectSoundResource(soundResource);
        }
        
        modelController.addEdit( new SelectEdit(modelController) );
    }
    
    /**
     * Resize selection box based on position of mouse
     * 
     * @param me MouseEvent that triggers this method.
     */
    private void resizeSelectionBox(MouseEvent me) {

        // mouse's current position
        int mouseX = me.getX();
        int mouseY = me.getY();

        // upper left corner of box
        int boxX;
        int boxY;

        int boxWidth;
        int boxHeight;

        // calculate new selection box bounds
        if (mouseX < clickX) {
            boxX = mouseX;
            boxWidth = clickX - mouseX;
        } else {
            boxX = clickX;
            boxWidth = mouseX - clickX;
        }
        if (mouseY < clickY) {
            boxY = mouseY;
            boxHeight = clickY - mouseY;
        } else {
            boxY = clickY;
            boxHeight = mouseY - clickY;
        }

        // actually resize here
        selectionBox.setBounds(boxX, boxY, boxWidth, boxHeight);
    }

    /**
     * Select everyone who intersects the selection box. Remove everyone who 
     * doesn't.
     */
    private void refreshSelectedList(MouseEvent me) {
    	
    	// Get the panel this listener listens for clicks on
    	CompositionPanel panel = (CompositionPanel)(me.getSource());

        Component[] components = panel.getComponents();
        Rectangle selectionRectangle = selectionBox.getBounds();

        // deselect all 
        modelController.clearSelection();

        // previousSelection will contain elements if we are control-dragging
        //    to draw a selection box
        for(SoundResource soundResource : previousSelection) {
            modelController.selectSoundResource(soundResource);
        }

        // select JComponents that the selectionBox intersects
        for (Component component : components) {
            if ( selectionRectangle.intersects(component.getBounds()) ) {
            	SoundResource soundResource = 
                    panel.getSoundResourceFromComponent((JComponent)component);
            	if( soundResource != null ){
                    modelController.selectSoundResource( soundResource );
            	}
            }
        }

        // update and bring the box to the front
        panel.setComponentZOrder(selectionBox, 0);
        panel.updateUI();
    }

    // unused interface methods
    public void mouseEntered(MouseEvent me) { }
    public void mouseExited(MouseEvent me) { }
    public void mouseMoved(MouseEvent me) { }
    public void mouseClicked(MouseEvent me) { }
}