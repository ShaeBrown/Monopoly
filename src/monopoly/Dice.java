/*
 * 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;
import java.util.Random;

/**
 * A pair of Die for the Game
 * 
 */
public class Dice {
    
    /**
     * The rolls of the previous first and second die
     */
    int roll1, roll2;
    /**
     * Were doubles just rolled
     */
    boolean doubles;

    private final Random r;
    
    /**
     * Creates a new pair of die
     */
    public Dice() {
        r = new Random();
        roll1=1;
        roll2=1;
    }
    
    /**
     * Performs a new roll
     * @return the sum of the dice rolled 
     */
    public int newRoll(){
        roll1 = roll();
        roll2 = roll();
        doubles = roll1 == roll2;
        return roll1 + roll2;
    }
    
    /**
     * Get the sum of the last roll
     * @return the sum of the two die
     */
    public int getRoll() {
        return roll1 + roll2;
    }
    
    /** Generates a random number
    * @return a number between 1-6
    */
    private int roll(){
        return r.nextInt(6) + 1;
    }
    
    /**
     * The roll of the first die
     * @return the first roll
     */
    public int getRoll1() {
        return roll1;
    }
    
    /**
     * The roll of the second die
     * @return the second roll
     */
    public int getRoll2() {
        return roll2;
    }
}
