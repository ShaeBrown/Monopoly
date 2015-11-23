/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 * The grids on the four corners of the board
 * @author Jacob
 */



public class GenericGrid extends Grid
{
    
    /**
     * Is the corner grid Go, Jail, FreeParking, or GoToJail
     */
    public enum GenericType { GO, JAIL, FREEPARKING, GOTOJAIL}
    
    private final GenericType grid_type;
    private int jackpot;
    
    /**
     * Creates a new generic (corner) grid
     * @param grid_type the type of corner
     */
    public GenericGrid(GenericType grid_type)
    {
        this.grid_type = grid_type;
        if (grid_type == GenericType.FREEPARKING)
            jackpot = 0;
    }
    
    /**
     * Increases the jackpot
     * @param amount amount to add to the jackpot
     */
    public void addToJackPot(int amount)
    {
        if (grid_type != GenericType.FREEPARKING) return;
        jackpot += amount;
    }
    
    /**
     * Performs the appropriate function for when the player lands on it.
     * Go, and Jail do nothing. Go to Jail sends player to Jail. If freeparking is
     * landed on, the player receives the jackpot
     * @param player the player who landed
     */
    @Override
    public void landingFunction(AbstractPlayer player)
    {
        switch(grid_type) {
            case GO:
            case JAIL:
                break;
            case FREEPARKING:
                player.addMoney(jackpot);
                Game.player_controller.displayMessage("Congratulations you won the jackpot of $" + jackpot);
                jackpot = 0;
                break;
            case GOTOJAIL:
                Game.player_controller.displayMessage("You must go to jail");
                player.setJailStatus(true);
                player.setLocation(Game.GRIDNUM.Jail.getNum());
                break;
        }
    }
}
