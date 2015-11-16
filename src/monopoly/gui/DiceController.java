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
public class DiceController implements ActionListener {
    
    ImageIcon[] die = new ImageIcon[6];
    Dice d;
    public JButton b1,b2;
    public boolean enabled;
    final Object LOCK;
    
    public DiceController(Dice d) 
    {
        this.d = d;
        for (int i = 0; i < die.length; i++) {
            die[i] = new ImageIcon(getClass().getResource("/monopoly/gui/img/dice/dice" + (i+1) + ".png"));
        }
        enabled = true;
        LOCK = new Object(); // just something to lock on
    }
    
    public void addButtons(JButton b1, JButton b2) 
    {
        this.b1 = b1;
        this.b2 = b2;
    }
    
    public Object getLock() 
    {
        return this.LOCK;
    }
    
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
  
    public void enable() {
        enabled = true;
    }
    
    public boolean isEnabled() 
    {
        return enabled;
    }
    
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
