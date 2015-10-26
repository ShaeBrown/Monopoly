/*
    The grid class is used to represents each of the grids used in on a gameboard.
    GridType defines all possible types of grids, each of them with a unique landing function.
        -The GENERIC type does nothing when a player lands on it. Examples includes Free Parking and Go. 
        -The PROPERTY type indicates a buyable property that can be developed on, such as Mediterranean Ave. 
            A seperate PropertyGrid class is used to store methods and data specific to this type.
        -The UTILITY type indicates a buyable property that CANNOT be developed on, and uses diceroll as a multiplier to rent. Electric company and waterworks are the only utilities in the official std monopoly. 
            A seperate UtilityGrid class is used to store methods and data specific to this type.
        -The RAILROAD type indicates a buyable property that CANNOT be developed on, and rent depends on the number of other railroads owned. Examples include Reading Railroad.
            A seperate RailroadGrid class is used to store methods and data specific to this type.
        -The CHANCE type indicates a player gets chance card when landed on. 
            There is no seperate class for Chance grids.
        -The COMCHEST type indicates a player gets community chest card when landed on. It essentially works the same as Chance 
            There is no seperate class for comchest grids.
        -The TAX type indicates a player gets some of his/her money deducted when landed on. Examples includes Income Tax.
            A seperate TaxGrid class is used to store methods and data specific to this type.
        -The GOTOJAIL type sends the player to jail. There is only one grid on the board.
            There is no seperate class for this grid.

        - GridType has been deprecated, and the actions should be implemented in the subclasses via dynamic binding

    Whenever a player lands on any grid, the landingFunction() method in this class should be called. The function then calls the appropriate handler that performs the correct grid function, depending on the type of this grid.
*/
package monopoly;

import java.util.LinkedList;

public abstract class Grid {
    
    protected String name;
    LinkedList<Player> occupants = new LinkedList();
    /*Add more class methods below?*/
    
    /*Calls an appropriate player landing function depending on the type of the grid*/
    abstract void landingFunction(Player player); 
    
    public LinkedList<Player> getOccupants() {
       return this.occupants;
    }
    
    public void addOccupant(Player p) {
        occupants.add(p);
    }
    
    public void removeOccupant() {
        try {
            occupants.removeFirst();
        }
        catch(Exception e) {}
    }
}
