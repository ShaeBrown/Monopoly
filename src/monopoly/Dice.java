/*
 * 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;
import java.util.Random;

public class Dice {
    
    int roll1, roll2;
    boolean doubles;
    private Random r;
    
    public Dice() {
        r = new Random();
        roll1=1;
        roll2=1;
    }
    
    public int newRoll(){
        roll1 = roll();
        roll2 = roll();
        doubles = roll1 == roll2;
        return roll1 + roll2;
    }
    
    public int getRoll() {
        return roll1 + roll2;
    }
    
    private int roll(){
        return r.nextInt(6) + 1;
    }
    
    public int getRoll1() {
        return roll1;
    }
    
    public int getRoll2() {
        return roll2;
    }
}
