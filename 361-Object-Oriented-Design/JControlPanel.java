/*
 * File:       JControlPanel.java
 * Authors:     Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter, Dale Skrien
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       27 October
 */
package project8;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * This class is a JPanel displaying a palette of all icons and sounds for
 * creating and playing the composition in the interactive panel.
 *
 * @author Martha Witick
 * @author Peter Graham
 * @author Dale Skrien
 */
public class JControlPanel extends JPanel {

    /** Handles selection of buttons based on mouse clicks. */
    private ButtonSelectListener buttonListener;
    
    /** 
     * Keep a popup menu to pop up over this panel.
     * This is kept here so that new buttons can use the same popup. 
     */
    private JPopupMenu popup;

    /** Allows user to select which palette is visible. */
    private JComboBox categorySelector; // contains Strings

    /** Holds the various palettes, keyed by their names. */
    private HashMap<String,JPanel> categoryTable;

    /** This is the sound that the playExampleSound method plays. */
    private JSoundButton exampleSound;
    
    /** panel to store user defined buttons in and show in the palette */
    private JPanel userDefinedPanel;
    
    /** 
     * Constructor builds the control panel.
     * 
     * @param resources Resources to build the palette and panel from
     */
    public JControlPanel( HashMap<String, List<SoundResource>> resources )
    {	
        build( resources );
    }
    

    /** 
     * Select the first available button on this panel.
     */
    public void selectFirstButton() {

        // Identify the button to select
        JPanel selectedPalette = getSelectedPalette();
        
        if(selectedPalette.getComponents().length > 0){
            JButton currentButton = (JButton)selectedPalette.getComponent(0);
        	
            // simulate a generic click on the button to select it
            MouseEvent m = new MouseEvent(currentButton, 0, 0, 0, 0, 0, 1,
                    false);
            buttonListener.mouseClicked(m);
        }
    }

    /** 
     * Return a reference to the selected button on the current palette. 
     * 
     * @return Currently selected JButton.
     */
    public JButton getSelectedButton() {
        return buttonListener.getSelected();
    }

    /** 
     * Return a reference to the palette that is currently visible.
     * 
     * @return Currently visible JPanel palette.
     */
    public JPanel getSelectedPalette() {
        String categoryName = (String) categorySelector.getSelectedItem();
        return categoryTable.get(categoryName);
    }
    
    /**
     * Add a new JSoundButton to the User Defined panel that uses a new 
     * gesture created from the selected labels.
     * 
     * sound should preferably be a SoundResource.
     * 
     * @param name String to name the JSoundButton with.
     * @param sound a Sound associated with selected labels
     * @param model the Model to store the new gesture inside
     */
    public void addUserDefinedGesture( String name, Sound sound, 
            CompositionModel model) {
    	
    	// Only add this to the model's table if its a SoundResource
    	if( sound instanceof SoundResource ) {
    		model.addUserDefinedGesture( (SoundResource)sound );
    	}
    	
    	// create the button
    	JSoundButton button = new JSoundButton(name, sound);

    	// configure and add the button to the User Defined panel
        button.addMouseListener( buttonListener );
        button.addMouseListener(new SampleSoundPopupListener(this,popup));
        
    	userDefinedPanel.add(button);
    	userDefinedPanel.updateUI();
    }
    
    /**
     * Clear the palette of all User Defined gestures.
     */
    public void clearUserDefinedGestures() {
    	userDefinedPanel.removeAll();
    	userDefinedPanel.updateUI();
    }

    /** 
     * Play this panel's exampleSound. 
     */
    public void playExampleSound() {
        exampleSound.playSound();
    }

    /** 
     * Get a JSoundButton's sound and set it as the example sound.
     */
    public void setExampleSound(JSoundButton b) {
        exampleSound = b;
    }
    
    /**
     * Build and return the popup menu that will later be displayed to play 
     * example sounds.
     * 
     * @return JPopupMenu popup capable of playing sounds.
     */
    private JPopupMenu createPopUpMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Play");
        menuItem.addActionListener(new SampleSoundListener(this));
        popup.add(menuItem);
        return popup;
    }

    /**
     *  Build several palettes, each with several buttons.
     *  Display the first palette with the first button selected.
     *  Lastly, build a popup menu.
     *  
     *  @param resources Resources to build from
     */
    private void build( HashMap<String, List<SoundResource>> resources ) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // create a table of icon-filled panels, one for each category
        // key = category name, value = panel with all icons for that category
        categoryTable = new HashMap<String,JPanel>();
        
        // sort the keys of the resources hashmap alphabetically
        List<String> categoryNames = new ArrayList<String>(resources.keySet() );
        Collections.sort( categoryNames );
        
        // create a combo box with category names for different button panels
        categorySelector = new JComboBox( categoryNames.toArray() );
        categorySelector.addActionListener(new PaletteSelectListener(this));
        add(categorySelector); // add the combo box to the control panel

        popup = createPopUpMenu();

        // buttonListener for all the buttons
        buttonListener = new ButtonSelectListener(new JButton());

        // create all the palettes and add them to the categoryTable
        for (String categoryName : categoryNames) {
            final JPanel panel = new JPanel(); //the new panel for this category
            categoryTable.put(categoryName, panel);
            panel.setLayout(new GridLayout(0, 2));
            panel.setPreferredSize(new Dimension(80, 338));

            // add a JSoundButton with each icon/sound pair
            for( SoundResource soundResource : resources.get(categoryName)) {
            	JButton currentButton =
                    new JSoundButton(soundResource.getIcon(), soundResource);
            	currentButton.addMouseListener( buttonListener );
            	currentButton.addMouseListener(new SampleSoundPopupListener(
                    this,popup));
            	panel.add(currentButton);
            }
        }
        
        // add the User Defined category
        String userDefined = "User Defined";
        userDefinedPanel =  new JPanel();
        userDefinedPanel.setLayout(new GridLayout(0, 2));
        userDefinedPanel.setPreferredSize(new Dimension(80, 338));
        categorySelector.addItem(userDefined);
        categoryTable.put(userDefined, userDefinedPanel);

        // initialize to contain first palette with first button selected
        String firstCategoryName = (String) categorySelector.getItemAt(0);
        add(categoryTable.get(firstCategoryName));
        selectFirstButton();
    }
}