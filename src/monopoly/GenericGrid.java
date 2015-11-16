/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 *
 * @author Jacob
 */



public class GenericGrid extends Grid
{
    
    public enum GridType {GO, JAIL, FREEPARKING, GOTOJAIL}
    
    private final GridType grid_type;
    
    public GenericGrid(GridType grid_type)
    {
        this.grid_type = grid_type;
    }
    
    @Override
    public void landingFunction(Player player)
    {
        switch(grid_type) {
            case GO:
            //nothing??
            case JAIL:
            //just visiting
            case FREEPARKING:
            
            case GOTOJAIL:
                player.setJailStatus(true);
                player.setLocation(Game.GRIDNUM.Jail.getNum());
        }
    }
}
