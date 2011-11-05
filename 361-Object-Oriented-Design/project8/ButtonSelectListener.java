/*
 * File:       ButtonSelectListener.java
 * Authors:    Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       10 December
 */
package project8;

import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.border.*;

/**
 * Implements a MouseListener to control which JButton is selected on the
 * palette this listener is attached to. It knows which button is selected.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class ButtonSelectListener implements MouseListener {

    /** Currently selected JButton. */
    private JButton selected;

    /** Red border used on the selected JButton.*/
    private LineBorder redBorder;
    
    /** the default border of the button when unselected */
    private Border unselectedBorder;

    /**
     * ButtonSelectListener's constructor,
     * sets selected JButton to newSelected and initializes fields.
     *
     * @param newSelected JButton to select on this object's creation.
     */
    public ButtonSelectListener(JButton newSelected) {
        selected = newSelected;
        redBorder = new LineBorder(Color.RED, 3);
        unselectedBorder = newSelected.getBorder();
    }

    /**
     * Returns the currently selected JButton.
     * 
     * @return Currently selected JButton.
     */
    public JButton getSelected() {
        return selected;
    }

    /**
     * Select the newly clicked JButton and deselect the old selected JButton.
     * Sets borders accordingly.
     * 
     * @param e MouseEvent whose location determines the newly selected JButton.
     */
    public void mouseClicked(MouseEvent e) {

        // See which button the MouseEvent was on
        JButton newSelected = (JButton) (e.getSource());
        
        // set the border to unselected default
        selected.setBorder(unselectedBorder);
        
        // give the newly selected button a red border
        selected = newSelected;
        selected.setBorder(redBorder);
    }

    // unused MouseListener methods
    public void mouseEntered(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}