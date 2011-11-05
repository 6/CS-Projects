/*
 * File:       Main.java
 * Recent Authors: Martha Witick, Peter Graham
 * Prv. Authors: Peter Williams, Leah Perlmutter, Katherine Smith, Will O'Brien
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       10 December
 */
package project8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.swing.border.LineBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class constructs the main components of the BattleOfTheClipsounds
 * application and starts them communicating with each other.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class Main
{
    // Test for the existence of an AudioSystem
    private static void testForAudioSystem() {
        try {
            if (AudioSystem.getMixer(null) == null) {
                System.err.println("AudioSystem Unavailable, exiting.");
                System.exit(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    // Build the menu bar
    // Needs frame, panel and playBar as aregs because listeners for
    //   menu items use panel and playBar, needs the frame to draw dialogs
    private static JMenuBar buildMenuBar(CompositionPanel interactivePanel,
            CompositionModel model,
            CompositionModelController modelController,
            MoveableRedBarManager playBar,
            JControlPanel control,
            Frame frame,
            FileHandler fileHandler) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = buildFileMenu( frame, modelController, fileHandler, 
            control );
        JMenu actionMenu = buildActionMenu(modelController, playBar);
        JMenu editMenu = buildEditMenu(frame, interactivePanel, model, 
        		modelController, control, fileHandler);
        JMenu resolutionMenu = buildResolutionMenu(modelController);
        JMenu guideMenu = buildGuideMenu(interactivePanel, modelController);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(actionMenu);
        menuBar.add(resolutionMenu);
        menuBar.add(guideMenu);

        return menuBar;
    }

    /**
     * Build and return a file menu.
     *
     * @param frame Frame to build dialog boxes in
     * @param modelController CompositionModelController to save and load from
     * @param fileHandler to handle files with
     *
     * @return JMenu fileMenu
     */
    private static JMenu buildFileMenu( Frame frame,
            CompositionModelController modelController, FileHandler fileHandler,
            JControlPanel controlPanel) {

        // Build file menu
        JMenu fileMenu = new JMenu("File");

        // Open item
        JMenuItem openMenuItem = new JMenuItem("Open...");
        FileLoadListener fileLoadListener = new FileLoadListener( frame,
                        modelController, controlPanel, fileHandler);
        openMenuItem.addActionListener(fileLoadListener);

        // Save item
        JMenuItem saveMenuItem = new JMenuItem("Save");
        FileSaveListener fileSaveListener = new FileSaveListener( frame,
                        modelController, fileHandler );
        saveMenuItem.addActionListener(fileSaveListener);
        saveMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // Exit item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ExitListener());

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    /**
     * Build and return the action menu.
     *
     * @param modelController - the composition model controller
     * @param playBar - the red bar to move across while playing
     * @return JMenu actionMenu
     */
    private static JMenu buildActionMenu(
                    CompositionModelController modelController,
                    MoveableRedBarManager playBar) {

        // Build action menu
        JMenu actionMenu = new JMenu("Action");

        // Play menu item
        JMenuItem playMenuItem = new JMenuItem("Play");
        playMenuItem.addActionListener(
                new PlayListener(modelController, playBar));
        playMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // Organize menu item
        JMenuItem organizeMenuItem = new JMenuItem("Organize Sounds");
        organizeMenuItem.addActionListener(
                        new OrganizeSoundListener(modelController) );
        organizeMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_B,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // Add menu items
        actionMenu.add(playMenuItem);
        actionMenu.addSeparator();
        actionMenu.add(organizeMenuItem);
        return actionMenu;
    }

    /**
     * Build and return the edit menu.
     *
     * @param frame to build dialogs over
     * @param panel to use as the view
     * @param model to create gestures on
     * @param modelController - the interactive composition model controller
     * @param control - the control panel
     * @param fileHandler to do XML stuff with
     * @return JMenu editMenu
     */
    private static JMenu buildEditMenu(Frame frame, CompositionPanel panel,
                    CompositionModel model,
                    CompositionModelController modelController,
                    JControlPanel control,
                    FileHandler fileHandler) {

        // Build edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(
                new DeleteListener(modelController));
        deleteMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.addActionListener(
                new LabelSelectListener(modelController));
        selectAllMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_A,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ));

        // Build the undo and redo parts of the edit menu
        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener( new UndoActionListener(
                modelController.getUndoManager()) );
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener( new RedoActionListener(
                modelController.getUndoManager()) );
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        // Build the cut, copy, and paste parts of the edit menu
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener( new CutListener(frame, modelController,
            fileHandler) );
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener( new CopyListener(frame, modelController,
            fileHandler) );
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener( new PasteListener(frame,
            modelController, fileHandler) );
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        // Build the repeater part of the edit menu
        JMenuItem repeaterMenuItem = new JMenuItem("Repeat selected sounds...");
        repeaterMenuItem.addActionListener(
                        new RepeaterListener(panel, modelController) );
        repeaterMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_R,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // Build the gesture creation part of the edit menu
        JMenuItem gestureMenuItem = new JMenuItem("Create Gesture");
        gestureMenuItem.addActionListener( new CreateGestureListener(
                panel, control, model) );
        gestureMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        editMenu.addSeparator();
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(deleteMenuItem);
        editMenu.add(selectAllMenuItem);
        editMenu.addSeparator();
        editMenu.add(repeaterMenuItem);
        editMenu.addSeparator();
        editMenu.add(gestureMenuItem);
        return editMenu;
    }

    /**
     * Build and return the resolution menu.
     *
     * @param modelController - the interactive composition model controller
     * @return JMenu resolution menu
     */
    private static JMenu buildResolutionMenu(CompositionModelController
            modelController) {
        // Build the resolution changing menu
        JMenu resolutionMenu = new JMenu("Resolution");
        JRadioButtonMenuItem veryHighRadioButtonMenuItem =
                new JRadioButtonMenuItem("Very High");
        JRadioButtonMenuItem highRadioButtonMenuItem =
                new JRadioButtonMenuItem("High");
        JRadioButtonMenuItem mediumRadioButtonMenuItem =
                new JRadioButtonMenuItem("Medium", true);
        JRadioButtonMenuItem lowRadioButtonMenuItem =
                new JRadioButtonMenuItem("Low");
        JRadioButtonMenuItem veryLowRadioButtonMenuItem =
                new JRadioButtonMenuItem("Very Low");

        resolutionMenu.add(veryHighRadioButtonMenuItem);
        resolutionMenu.add(highRadioButtonMenuItem);
        resolutionMenu.add(mediumRadioButtonMenuItem);
        resolutionMenu.add(lowRadioButtonMenuItem);
        resolutionMenu.add(veryLowRadioButtonMenuItem);

        ResolutionListener resolutionListener =
                new ResolutionListener(modelController);

        veryHighRadioButtonMenuItem.addActionListener(resolutionListener);
        highRadioButtonMenuItem.addActionListener(resolutionListener);
        mediumRadioButtonMenuItem.addActionListener(resolutionListener);
        lowRadioButtonMenuItem.addActionListener(resolutionListener);
        veryLowRadioButtonMenuItem.addActionListener(resolutionListener);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(veryHighRadioButtonMenuItem);
        buttonGroup.add(highRadioButtonMenuItem);
        buttonGroup.add(mediumRadioButtonMenuItem);
        buttonGroup.add(lowRadioButtonMenuItem);
        buttonGroup.add(veryLowRadioButtonMenuItem);
        return resolutionMenu;
    }

    /**
     * Build and return the guides menu.
     *
     * @param panel - the interactive composition panel
     * @param modelController - the interactive composition model controller
     * @return JMenu guide menu
     */
    public static JMenu buildGuideMenu( CompositionPanel panel,
            CompositionModelController modelController) {

        JMenu guideMenu = new JMenu("Guides");

        JCheckBoxMenuItem enableSnapTo = new JCheckBoxMenuItem("Snap To Grid");
        enableSnapTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

        JMenuItem setGuideDelay = new JMenuItem("Set grid distance (ms)...");
        setGuideDelay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );


        SnapToListener snapToListener = new SnapToListener(modelController);
        GuideDelayListener guideDelayListener = new GuideDelayListener(panel,
                        modelController);

        enableSnapTo.addItemListener(snapToListener);
        setGuideDelay.addActionListener(guideDelayListener);

        guideMenu.add(enableSnapTo);
        guideMenu.addSeparator();
        guideMenu.add(setGuideDelay);

        return guideMenu;
    }

    /**
     * Build the interactive composition panel.
     * @return the interactive panel
     */
    private static CompositionPanel buildCompositionPanel() {
        CompositionPanel interactivePanel = new CompositionPanel();
        interactivePanel.setBorder(new LineBorder(Color.BLACK, 1));
        interactivePanel.setLayout(null);

        return interactivePanel;
    }

    /**
     *  Initialize communication between panels and classes that perform actions
     *  on them.
     * @param control the control panel
     * @param panel interactive panel
     * @param modelController the model controller
     */
    private static void initializeCommunication(JPanel control, 
            CompositionPanel panel,
            CompositionModelController modelController) {

        // create some listeners
        MouseListener labelCreationListener =
                new LabelCreationListener(control, modelController);
        MouseListener labelDragListener = 
        		new LabelDragListener(modelController);
        LabelSelectListener selectListener =
                new LabelSelectListener( modelController);
        
        ComponentListener panelResizeListener = 
            new CompositionPanelResizeListener(modelController);

        // attach them to the panels
        panel.addMouseListener(labelCreationListener);
        panel.addMouseListener(selectListener);
        panel.addMouseMotionListener(selectListener);
                panel.addMouseListener(labelDragListener);
        panel.addMouseMotionListener(
                (MouseMotionListener)labelDragListener);
        
        panel.addComponentListener(panelResizeListener);
    }

    /**
     * Set additional attributes on the frame
     * @param frame the JFrame which holds the menubar, control panel, and
     * 		  interactive panel
     */
    private static void configureFrame(JFrame frame) {
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setLocation(Constants.WINDOW_X, Constants.WINDOW_Y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Set up a barebones console and play the given file at shortFilename
     * 
     * @param shortFilename Name of file to play
     */
    private static void runConsoleApplication(String shortFilename) {
    	
    	System.out.println("Start console");
    	
    	// Set up and play the file at shortFilename
    	CompositionModel compositionModel = new CompositionModel();
    	FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(compositionModel);

            // Only load if the filename is not null and not invalid
            if( shortFilename != null ) {

                List<SoundResource> soundResources =
                    fileHandler.loadFromFile(shortFilename);

                // be lazy and play the soundImages here for now
                System.out.println(shortFilename + " now playing.");
                compositionModel.add(soundResources, 0);
                compositionModel.play();

                System.out.println(compositionModel.getSoundResources());

                System.out.println( shortFilename + " loaded successfully.");
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("Parser not configured correctly. Try again?");
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("Please specify a valid save to load.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO error. Please try again or check the file.");
        }
    }
    
    /**
     * Build and run the GUI version of this application
     */
    private static void runGUIApplication() {
        // Mac-specific: moves menu bar to top of screen
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // Construct and configure the GUI elements
        JFrame frame = new JFrame("Battle of the ClipSounds");
        configureFrame(frame);

        // panel to hold everything
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );

        // interactive panel
        CompositionPanel compositionPanel = buildCompositionPanel();

        // build the model
        CompositionModel compositionModel = new CompositionModel(
            compositionPanel);

        // build the model controller
        CompositionModelController compositionModelController =
            new CompositionModelController( compositionModel );

        // control panel
        JControlPanel control = new JControlPanel(
            compositionModel.getMasterResources());

        // playbar
        MoveableRedBarManager playBar = new MoveableRedBarManager(
                compositionPanel, compositionModelController);

        // build the file handler
        try {
            FileHandler fileHandler = new FileHandler(control,
                            compositionModel);

            // menu bar
            JMenuBar menuBar = buildMenuBar(compositionPanel, compositionModel,
                    compositionModelController, playBar, control, frame,
                    fileHandler);

            // Compose GUI elements
            frame.setJMenuBar(menuBar);

        } catch (ParserConfigurationException e) {
                e.printStackTrace();
                System.out.println("Parser configured incorrectly. Try again?");
        }

        // scroll pane to hold the interactive panel
        JScrollPane scroll = new JScrollPane(compositionPanel);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        scroll.setPreferredSize( new Dimension(
                Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));

        frame.add(topPanel);
        topPanel.add(scroll);
        topPanel.add(control, BorderLayout.WEST);
        topPanel.updateUI();

        // Initialize communication between panels and classes that
        //   perform actions on them
        initializeCommunication(control, compositionPanel,
                compositionModelController);
    }
	
    /**
     * Start the BattleOfTheClipsounds application.
     * 
     * @param args Give "-p" "file.xml" to load and play file.xml on the console
     */
    public static void main(String[] args)
    {
        // Test for the existence of an AudioSystem
        testForAudioSystem();
        
        // Know if the program has been run or not
        boolean run = false;
        
        // parse all commands
        for( int i=0; i<args.length; i++) {
        	
            // if -h is found, print the help
            if( args[i].equals("-h") ) {
                System.out.println(
                "Battle of the Clipsounds v.p7" +
                "\nInteractive Sound Composition Application\n" +
                "\n -p file.xml     Play given file on the commandline." +
                "\n -h              Print this help file.\n " +
                "If no known command is given, start the GUI application.\n");

                run = true; // do not let help and GUI happen at the same time
            }

            // if -p is found... play the given file from the commandline
            else if( args[i].equals("-p") && i+2 <= args.length) {
                // use the next parameter as a filename if possible
                runConsoleApplication( args[i+1] );
                i++;
                run = true;
            }
        }
        
        // If nothing has been run... start the GUI application
        if(!run) {
            runGUIApplication();
        }
    }
// end main class
}