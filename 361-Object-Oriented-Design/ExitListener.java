/*
 * File:       ExitListener.java
 * Authors:    Martha Witick, Peter Graham
 * Previous Authors: Katherine Smith, Leah Perlmutter, Peter Williams
 * Class:      CS 361, Fall 2010
 * Project:    8
 * Date:       6 November
 */
package project8;

import java.awt.event.*;

/**
 * This class provides an action listener that prints a message to
 * the console window when an event occurs.
 * 
 * @author Martha Witick
 * @author Peter Graham
 */
public class ExitListener implements ActionListener
{
    /**
     * Close current application.
     * 
     * @param e ActionEvent that triggers this method.
     */
    public void actionPerformed(ActionEvent e)
    {
      System.exit(0);
    }
}