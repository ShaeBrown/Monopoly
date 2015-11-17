
package monopoly;

import java.util.LinkedList;

/**
 * The grid class is used to represents each of the grids used in on a gameboard.
 * GridType defines all possible types of grids, each of them with a unique landing function. <br>
 *       -The GENERIC type does nothing when a player lands on it. Examples includes Free Parking and Go. <br>
 *       -The PROPERTY type indicates a buyable property that can be developed on, such as Mediterranean Ave. <br>
 *           A seperate PropertyGrid class is used to store methods and data specific to this type. <br>
 *       -The UTILITY type indicates a buyable property that CANNOT be developed on, 
 *          and uses diceroll as a multiplier to rent. Electric company and waterworks are the only utilities in the official std monopoly.
 *           A seperate UtilityGrid class is used to store methods and data specific to this type. <br>
 *       -The RAILROAD type indicates a buyable property that CANNOT be developed on, and rent depends on the number of other railroads owned. Examples include Reading Railroad. <br>
 *           A seperate RailroadGrid class is used to store methods and data specific to this type. <br>
 *       -The CHANCE type indicates a player gets chance card when landed on. <br>
 *           There is no seperate class for Chance grids. <br>
 *       -The COMCHEST type indicates a player gets community chest card when landed on. It essentially works the same as Chance 
 *           There is no seperate class for comchest grids. <br>
 *       -The TAX type indicates a player gets some of his/her money deducted when landed on. Examples includes Income Tax.
 *           A seperate TaxGrid class is used to store methods and data specific to this type. <br>
 *       -The GOTOJAIL type sends the player to jail. There is only one grid on the board.
 *           There is no seperate class for this grid. <br>
 *          <br>
 *       - GridType has been deprecated, and the actions should be implemented in the subclasses via dynamic binding <br>
 *<br>
 *   Whenever a player lands on any grid, the landingFunction() method in this class should be called. The function then calls the appropriate handler that performs the correct grid function, depending on the type of this grid.
 * 
 */

public abstract class Grid {
    
    /**
     * the name of the grid
     */
    protected String name;
    LinkedList<AbstractPlayer> occupants = new LinkedList();
    /*Add more class methods below?*/
    
    /*Calls an appropriate player landing function depending on the type of the grid*/
    abstract void landingFunction(AbstractPlayer player); 
    
    /**
     * Get the name of the grid
     * @return the grid's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * The number of players occupying one grid. Is kept track of so that players
     * can be properly spaced out on the GUI, when sharing the same grid.
     * @return the list of players on that grid
     */

    public LinkedList<AbstractPlayer> getOccupants() {
       return this.occupants;
    }
    
    /**
     * Adds an occupant to the grid
     * @param player the player to add to the grid
     */
    public void addOccupant(AbstractPlayer player) {
        occupants.add(player);
    }
    
    /**
     * Removes the next player to leave from the list of occupants
     */
    public void removeOccupant() {
        try {
            occupants.removeFirst();
        }
        catch(Exception e) {} //something went wrong
    }
}
