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

/**
 *
 * @author shaebrown
 */
public class DiceView implements ActionListener {
    
    ImageIcon[] die = new ImageIcon[6];
    Dice d;
    JButton b1,b2;
    
    public DiceView(Dice d, JButton b1, JButton b2) {
        this.d = d;
        this.b1 = b1;
        this.b2 = b2;
        for (int i = 0; i < die.length; i++) {
            die[i] = new ImageIcon(getClass().getResource("/monopoly/gui/img/dice" + (i+1) + ".png"));
        }
        b1.setIcon(die[0]);
        b2.setIcon(die[0]);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        d.getRoll();
        b1.setIcon(getDiceImage1(d));
        b2.setIcon(getDiceImage2(d));
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
