/*
 * File: LabelDragListener.java
 * Recent Authors: Martha Witick, Peter Graham
 * Author: Leah Perlmutter, Katherine Smith, Will O'Brien
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *  This listener listens for user input and drags labels accordingly. It knows
 *  the CompositionModel to drag labels on, carries a list of label offsets
 *  from the last mouse click, if the mouse is in the frame or not,  and the 
 *  location of the last mouse press.
 *  
 * @author Martha Witick
 * @author Peter Graham
 */
public class LabelDragListener implements MouseListener, MouseMotionListener{

    /** The model to apply changes to. */
    private CompositionModelController modelController;

    /** Whether mouse is in frame (prevents user from dragging labels off) */
    private boolean mouseInFrame;

    /** 
     * Difference between mouse click and locations of clicked components.
     * Position in offsets correlates with position in the model's selected.
     * All offsets are absolute.
     */
    private List<Point> offsets;
    
    /** Location of mouse press. */
    private Point click;
    
    /** Is true if the component we pressed on is selected */
    private boolean isComponentSelected;

    /** 
     * Initialize fields and set this CompositionModel to m.
     * 
     * @param cmc CompositionModelController to move SoundResources on.
     */
    public LabelDragListener(CompositionModelController cmc) {
        modelController = cmc;
        offsets = new ArrayList<Point>();
        isComponentSelected = false;
    }

    /**
     * Set mouseInFrame to true when mouse enters panel.
     *
     * @param e MouseEvent that triggers this method.
     */
    public void mouseEntered(MouseEvent e) {
        mouseInFrame = true;
    }

    /**
     * Set mouseInFrame to false when mouse leaves panel.
     * 
     * @param e MouseEvent that triggers this method.
     */
    public void mouseExited(MouseEvent e) {
        mouseInFrame = false;
    }

    /**
     * Calculate and store offsets for all selected components.
     * Also, store press location in this.click.
     *
     * @param e MouseEvent to get the press location from.
     */
    public void mousePressed(MouseEvent e) {
    	JPanel panel = (JPanel)(e.getSource());
    	
        JComponent pressedComponent = 
            (JComponent)(panel.getComponentAt(e.getX(), e.getY()));
        
        // If pressed component is selected, set isComponentSelected to true
        LineBorder border = (LineBorder)(pressedComponent.getBorder());
        isComponentSelected = ( border != null && 
            border.getLineColor() == Color.yellow);
        
        // keep the absolute location of the press in click
        click = modelController.calculateAbsoluteFromPixel(
            new Point(e.getX(), e.getY()) );

        // if we've pressed on a selected item, prepare to drag
        if( isComponentSelected ) {
            for( SoundResource soundResource : modelController.getSelected() ) {
                // calculate and save each offset as a point inside offsets
                offsets.add( new Point( 
                	(int)(click.getX() - soundResource.getDelay()),
                        (int)(click.getY() - soundResource.getTrack()) ) );
            }
        }
    }

    /** 
     * If mouse is dragging on a JComponent, move selected JComponents 
     * with the mouse.
     * 
     * @param e MouseEvent to calculate a location from.
     */
    public void mouseDragged(MouseEvent e) {

        // get panel's list of selected
        List<SoundResource> selected = modelController.getSelected();
        
        // convert e's location to absolute
        Point event = modelController.calculateAbsoluteFromPixel(e.getPoint());
        
        if (  !modelController.isSelecting() &&  // drag mode,not selection mode
              mouseInFrame &&
              !modelController.isFrozen() &&
              isComponentSelected  ) {

            // drag everything in selected
            for (int i = 0; i < selected.size(); i++) {
            	SoundResource soundResource = selected.get(i);
            	
                soundResource.setLocation( new Point(
                        (int) (event.getX() - offsets.get(i).getX()),
                        (int) (event.getY() - offsets.get(i).getY()) ));
                
                // update each individual label's location
                modelController.updateLocation(soundResource);
            }
            
            // update changes
        }
    }

    /**
     *  On mouse release, clear all offsets. Also, create a MoveEdit with
     *  our action if something's been dragged.
     *  
     *  @param e MouseEvent to get the location of release from.
     */
    public void mouseReleased(MouseEvent e) {
        offsets.clear();
        isComponentSelected = false;
        
        // get the absolute current mouse location
        Point event = modelController.calculateAbsoluteFromPixel(e.getPoint());
        
        // calculate the offset between the current mouse location and click
        Point offset = new Point( (int) (event.getX() - click.getX()),
        		         (int) (event.getY() - click.getY()) );
        
        // only make a new undo edit if something's been dragged
        if( !modelController.isSelecting() && 
            (offset.getX() != 0.0 || offset.getY() != 0.0) &&
            !modelController.getSelected().isEmpty() ){
        	
            // if something's been dragged and snapto is enabled
            if( modelController.isSnapToEnabled() ) {
            	// snap the leftmost selected label to the closest left line
            	// TODO is math right?
            	List<SoundResource> selected = modelController.getSelected();
            	
            	
            	// get the leftmost label
            	SoundResource leftmost = modelController.lowestDelay(selected);
            	
            	// find distance between closest (left) guideline and leftmost
            	int distance;
            	if( 0 <= leftmost.getDelay() ) {
                    distance = -leftmost.getDelay() %
                    modelController.getGuideDelay();
            	}
            	else {
                    distance = -leftmost.getDelay();
            	}
            	
            	// snap all selected labels to guideline
            	for(SoundResource soundResource : selected) {
                    Point soundPosition = soundResource.getAbsolutePosition();
                    soundResource.setLocation((int)soundPosition.getX()
                        + distance, (int)soundPosition.getY());
            	}
            	
            	// adjust offset
            	offset.setLocation(offset.getX() + distance, offset.getY());
            	modelController.updateLocation(selected);
            }
        	
            // Create new edit and add it to the undo manager, hand over the
            // offset between click and current release location.
            modelController.addEdit( new MoveEdit(modelController, offset) );
        }
    }
    

    // unused methods from the MouseListener interface
    public void mouseClicked(MouseEvent e) {  }
    public void mouseMoved(MouseEvent e) {  }
}