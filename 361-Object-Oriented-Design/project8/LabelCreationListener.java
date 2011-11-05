/*
 * File: LabelCreationListener.java
 * Authors: Martha Witick, Peter Graham
 * Previous Authors: Leah Perlmutter, Katherine Smith, Will O'Brien
 * Class: CS 361, Fall 2010
 * Project: 8
 * Date: 6 November
 */
package project8;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Subclasses a MouseListener to handle events that create SoundResources at the
 * appropriate absolute location in the CompositionModel. Knows the controlPanel
 * that knows which type of label to make and knows a CompositionModelController 
 * to add the SoundResources onto.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class LabelCreationListener implements MouseListener {

    /** The model to add soundResources to */
    private CompositionModelController modelController;

    /** The controlPanel with buttons that determine which label is created. */
    private JPanel toolBox;

    /**
     * Constructor that initializes fields.
     * 
     * @param t JPanel that selects which type of label to create.
     * @param cmc CompositionModelController to create soundResources for.
     */
    public LabelCreationListener(JPanel t, CompositionModelController cmc) {
        toolBox = t;
        modelController = cmc;
    }

    /**
     * For every click on background, create a new SoundResource at the absolute 
     * click location on the view. Don't create a new SoundResource if the model 
     * is frozen or if we didn't click on the view panel.
     * 
     * @param e MouseEvent to test is we clicked on the panel or not.
     */
    public void mouseClicked(MouseEvent e) {
    	JPanel panel = (JPanel)(e.getSource());
        if  (  modelController.isFrozen() == false &&
               panel.getComponentAt(e.getX(), e.getY()) == panel ) {

            createLabel(e);
        }
    }

    /** 
     * Create a soundResource where the user clicked, deselect everyone else if 
     * the meta key isn't pressed, and select the new soundResource.
     * 
     * @param e MouseEvent to retrieve the new soundResource location from.
     */
    private void createLabel(MouseEvent e) {

        // get icon and sound attributes from the selected button
        JButton currentButton = ((JControlPanel)toolBox).getSelectedButton();
        Icon currentIcon = currentButton.getIcon();
        Sound currentSound = ((JSoundButton)currentButton).getSound();
        
        // make a new SoundResource and put it in the model
        // display a string if the label lacks an icon
        SoundResource soundResource;
        Point absolute = modelController.calculateAbsoluteFromPixel( 
            e.getPoint() );
        if(currentIcon == null){
            soundResource = new SoundResource( currentButton.getText(), 
                currentSound, (int)absolute.getX(), (int)absolute.getY() );
        }
        else {
            //work under the assumption that all master Sounds = SoundResources
            soundResource = new SoundResource( (SoundResource)currentSound, 
                (int)absolute.getX(), (int)absolute.getY() );
        }
        
        // make a new sound label and put it on the interactive panel
        modelController.add(soundResource);

        // deselect everyone else and select soundBox
        if(!e.isMetaDown()) {
            modelController.clearSelection();
        }
        modelController.selectSoundResource(soundResource);
        
        // add this edit to the undo manager
        modelController.addEdit(new AddEdit(modelController, soundResource));
        modelController.addEdit(new SelectEdit(modelController, false, true));
    }

    // Unused methods from the MouseListener interface
    public void mouseEntered(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}