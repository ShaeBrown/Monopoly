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

    Whenever a player lands on any grid, the landingFunction() method in this class should be called. The function then calls the appropriate handler that performs the correct grid function, depending on the type of this grid.
*/
package monopoly;

public class Grid {
    
    enum GridType {GENERIC, PROPERTY, UTILITY, RAILROAD, CHANCE, COMCHEST, TAX, GOTOJAIL}
    
    GridType grid_type;              //What type of grid is this?
    String name;                     //Name of the grid?
    PropertyGrid property = null;    //Only to be used by PROPERTY type
    RailroadGrid railroad = null;   //Only to be used by RAILROAD type
    UtilityGrid utility = null;     //Only to be used by UTILITY type
    TaxGrid tax = null;             //Only to be used by TAX type
    
    /*Default Constructor*/
    public Grid()
    {
        this.grid_type = GridType.GENERIC;
        this.name = null;
    }
    
    /*Generic Constructor*/
    public Grid(GridType grid_type, String name)
    {
        this.grid_type = grid_type;
        this.name = name;
    }
    
    /*Constructor for PROPERTY*/
    public Grid(PropertyGrid property)
    {
        this.grid_type = GridType.PROPERTY;
        this.name = property.getPropertyName();
        this.property = property;
    }
    
    /*Constructor for RAILROAD*/
    public Grid(RailroadGrid railroad)
    {
        this.grid_type = GridType.RAILROAD;
        this.name = railroad.getPropertyName();
        this.railroad = railroad;
    }
    
    /*Constructor for UTILITY*/
    public Grid(UtilityGrid utility)
    {
        this.grid_type = GridType.UTILITY;
        this.name = utility.getPropertyName();
        this.utility = utility;
    }
    
    /*Constructor for TAX*/
    public Grid(TaxGrid tax)
    {
        this.grid_type = GridType.TAX;
        this.name = tax.name;
        this.tax = tax;
    }
    
    /*Constructor for special events (GOTOJAIL, COMCHEST, CHANCE)*/
    public Grid(GridType grid_type)
    {
        switch(grid_type)
        {
            case GOTOJAIL:
                this.grid_type = GridType.GOTOJAIL;
                this.name = "GO TO JAIL";
                break;
            case COMCHEST:
                this.grid_type = GridType.COMCHEST;
                this.name = "COMMUNITY CHEST";
                break;
            case CHANCE:
                this.grid_type = GridType.CHANCE;
                this.name = "CHANCE";
                break;
            default:
                System.out.println("Wrong constructor called. Found: " +grid_type +" Expected GOTOJAIL, COMCHEST, or CHANCE");
                break;
        }
    }
    
    /*Add more class methods below?*/
    
    /*Calls an appropriate player landing function depending on the type of the grid*/
    void landingFunction(Player player)
    {
        switch (grid_type)
        {
            case PROPERTY:
                property.landingFunction(player);
                break;
            case UTILITY:
                utility.landingFunction(player);
                break;
            case RAILROAD:
                railroad.landingFunction(player);
                break;
            case TAX:
                tax.landingFunction(player);
                break;
            
            /*TODO: Implement all other grid_types (CHANCE, COMCEST, GOTOJAIL), EXCEPT for generic*/
            
            default:
                /*GENERIC TYPE: do nothing?*/
                break;  
        }
    }
 
}
