/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;

import javax.swing.*;
import monopoly.Dice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* DiceController
*   
*   - Holds an array of ImageIcons for each face of the die
*   - responds to when the user clicks the die, rolls new pair and sets images
*   - Hold a object which acts as a lock, to wait for when the next player chooses to roll
*   - Disables itself when the current player has already rolled once
*/

/**
* DiceController, controls the dice buttons on GUI.
*   <br>
*   - Holds an array of ImageIcons for each face of the die <br>
*   - Responds to when the user clicks the die, rolls new pair and sets images <br>
*   - Hold a object which acts as a lock, to wait for when the next player chooses to roll <br>
*   - Disables itself when the current player has already rolled once <br>
 * @author shaebrown
 */

public class DiceController implements ActionListener {
    
    ImageIcon[] die = new ImageIcon[6];
    Dice d;
    /*
    * The first dice buttons on the gui
    */
    public JButton b1;
    /*
    * The second dice buttons on the gui
    */
    public JButton b2;     

    /**
     * Is the button currently clickable
     */
    public boolean enabled;
    final Object LOCK;
    
    /**
     * Creates a new dice controller
     * @param d the die object to mimic on GUI
     */
    public DiceController(Dice d) 
    {
        this.d = d;
        for (int i = 0; i < die.length; i++) {
            die[i] = new ImageIcon(getClass().getResource("/monopoly/gui/img/dice/dice" + (i+1) + ".png"));
        }
        enabled = true;
        LOCK = new Object(); // just something to lock on
    }
    
    /**
     * Add the dice buttons to be controlled
     * @param b1 the first dice button
     * @param b2 the second dice button
     */
    public void addButtons(JButton b1, JButton b2) 
    {
        this.b1 = b1;
        this.b2 = b2;
    }
    
    /**
     * Gets the lock, which makes the game wait for the player to click on the die
     * @return the lock
     */
    public Object getLock() 
    {
        return this.LOCK;
    }
    
    /**
     * When clicked, the dice is rolled and the roll is displayed on the button.
     * It notifies the game to stop waiting for the player to press the button
     * @param event the event
     */
    @Override
    public void actionPerformed(ActionEvent event) 
    {
        if (enabled) {
            d.newRoll();
            displayRoll();
            enabled = false;
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }
    }
  
    /**
     * Allow the dice buttons to be clicked again
     */
    public void enable() {
        enabled = true;
    }
    
    /**
     * Is the dice button clickable?
     * @return
     */
    public boolean isEnabled() 
    {
        return enabled;
    }
    
    /**
     * Displays the dice roll on the buttons
     */
    public void displayRoll()
    {
        b1.setIcon(getDiceImage1(d));
        b2.setIcon(getDiceImage2(d));
    }
    
    private ImageIcon getDiceImage1(Dice d)
    {
        int r = d.getRoll1();
        return die[r-1];
    }
    
    private ImageIcon getDiceImage2(Dice d)
    {
        int r = d.getRoll2();
        return die[r-1];
    }
}
