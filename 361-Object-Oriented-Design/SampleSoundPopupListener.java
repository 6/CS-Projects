/*
 * File:       SampleSoundPopupListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors:     Katherine Smith, Leah Perlmutter
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 * Listens for a popup trigger and shows the popup menu on trigger.
 * Designed for use on JSoundButtons.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class SampleSoundPopupListener implements MouseListener {

    /** The JControlPanel to setExampleSound on. */
    private JControlPanel panel;

    /** The PopupMenu to display. */
    private JPopupMenu menu;

    /**
     * Initialize the fields.
     * @param p Set the JControlPanel panel.
     * @param m Set the popup menu to show.
     */
    public SampleSoundPopupListener(JControlPanel p, JPopupMenu m){
        panel = p;
        menu = m;
    }

    /**
     * Show popup on mouse press.
     * 
     * @param e MouseEvent to show popup on
     */
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Show popup on mouse release?
     * 
     * @param e MouseEvent to show popup on?
     */
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     *  If user clicked a sound button, show the popup.
     *  
     * @param e MouseEvent to get the location to show the popup at.
     */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {

            // e.getComponent() should only ever be a JSoundButton because
            // we only add this listener to JSoundButtons
            panel.setExampleSound((JSoundButton)e.getComponent());
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    // unused interface methods
    public void mouseClicked(MouseEvent me){ }
    public void mouseEntered(MouseEvent me) { }
    public void mouseExited(MouseEvent me) { }
}