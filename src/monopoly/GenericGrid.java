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
    
    private GridType grid_type;
    
    public GenericGrid(GridType grid_type)
    {
        this.grid_type = grid_type;
    }
    
    public void landingFunction(Player player)
    {
        switch(grid_type) {
            case GO:
            
            case JAIL:
            
            case FREEPARKING:
                
            case GOTOJAIL:
        }
    }
}
