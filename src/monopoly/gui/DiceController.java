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


public class DiceController implements ActionListener {
    
    ImageIcon[] die = new ImageIcon[6];
    Dice d;
    public JButton b1,b2;
    public boolean enabled;
    int roll;
    Object LOCK;
    
    public DiceController(Dice d) {
        this.d = d;
        for (int i = 0; i < die.length; i++) {
            die[i] = new ImageIcon(getClass().getResource("/monopoly/gui/img/dice/dice" + (i+1) + ".png"));
        }
        enabled = true;
    }
    
    public void addButtons(JButton b1, JButton b2) {
        this.b1 = b1;
        this.b2 = b2;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        if (enabled) {
            this.roll = d.getRoll();
            b1.setIcon(getDiceImage1(d));
            b2.setIcon(getDiceImage2(d));
            enabled = false;
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }
    }
    
    public int getRoll() {
        return roll;
    }
    
    public void enable() {
        enabled = true;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    public void addLock(Object LOCK) {
        this.LOCK = LOCK;
    }
    
    public ImageIcon getDiceImage1(Dice d){
        int r = d.getRoll1();
        return die[r-1];
    }
    
    public ImageIcon getDiceImage2(Dice d){
        int r = d.getRoll2();
        return die[r-1];
    }
}
