
package monopoly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 * Abstract player class which should be used to create all other players
 * @author Jacob Hodge
 */
public abstract class AbstractPlayer {
    
    enum PlayerToken{DOG, BATTLESHIP, AUTOMOBILE, TOPHAT, THIMBLE, BOOT, WHEELBARROW, CAT}
    
    /**
     *  The player's name
     */
    protected String name;

    /**
     * The token the player chose
     */
    protected PlayerToken token;

    /**
     *  How much money the player has
     */

    protected int money;

    /**
     * The number of the grid the player is currently on
     */
    protected int location;      

    /**
     *  The number of get out of jail free cards
     */
    protected int jail_free_cards; 

    /**
     *  Is the player in jail
     */
    protected boolean in_jail;

    /**
     * The list of properties the player currently owns
     */
    protected LinkedList<BuyableGrid> property;

    /**
     * The key is the property group (BLUE, UTILITY, ETC..) and the value is
     * how many of that type the player currently owns
     */
    protected HashMap<BuyableGrid.PropertyGroup, Integer> num_of_owned_type;
    

    /**
     *  The tasks to perform when the player begins its turn and rolls the die
     */

    public abstract void beginTurn();

    /**
     * The command to make the player roll the die
     * @return the dice roll
     */
    public abstract int rollDie();

    /**
     * How the player will decide to buy the property they have landed on
     */
    public abstract void propertyDecision();
    
    /**
     * How will the player decide to pay, use get out of jail free card, or try to roll doubles?
     */
    public abstract void jailDecision(); 
    
    /**
     * Any last tasks before the next players turn
     */
    public abstract void finalizeTurn(); 
    
    
    /*New Player constructor*/

    /**
     * Creates a new abstract player
     */

    public AbstractPlayer ()
    {
        this.name = "";
        this.token = PlayerToken.DOG;
        this.money = 0;
        this.location = 0;
        this.jail_free_cards = 0;
        this.in_jail = false;
        this.property = new LinkedList<>();
        Game.board_grids[0].addOccupant(this);
        num_of_owned_type = new HashMap<>();
        for (BuyableGrid.PropertyGroup group: BuyableGrid.PropertyGroup.values()) {
            num_of_owned_type.put(group, 0);
        }
    }
    
    /**
     * Abstract method to clone a player
     * @return a cloned player
     */

    public abstract AbstractPlayer clone();
    


    /**
     * Get the player's name
     * @return player's name
     */

    
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Sets the player's name
     * @param name the player's name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the amount of money the player has
     * @return the amount of money the player has
     */
    public int getMoney()
    {
        return this.money;
    }
    
    /**
     * Get a list of properties the player owns
     * @return a list of properties the player owns
     */
    public LinkedList<BuyableGrid> getProperties() 
    {
        return property;
    }
    
    /**
     * Sets the player's owned properties to a list of properties
     * @param properties the list of properties
     */
    public void setProperties(LinkedList<BuyableGrid> properties)
    {
        this.property = properties;
    }
    
    /**
     * Sets the player's money
     * @param money the amount of money
     */
    public void setMoney(int money)
    {
        this.money = money;
        updateStats();
    }
    
    /**
     * Increments to the player's money
     * @param money the amount of money to add to the player
     */
    public void addMoney(int money)
    {
        this.money += money;
        updateStats();
    }
    
    /**
     * Decrement to the player's money
     * @param money the amount of money to take from the player
     */
    public void removeMoney(int money) {
        this.money -= money;
        updateStats();
    }
    
    /**
     * Gets the number of the grid the player is on
     * @return the number of the grid the player is on
     */
    public int getLocation()
    {
        return this.location;
    }
    
    /**
     * Get the grid the player is currently on
     * @return the grid object the player is on
     */
    public Grid getCurrentGrid()
    {
        return Game.board_grids[getLocation()];
    }
    
    /**
     * Advances the player on the board by the amount
     * @param amount the amount the move the player
     */
    public void advance(int amount)
    {
        int new_location = getLocation() + amount;
        
        /*Did the rolling player "overflow" and passed GO?*/
        if(new_location > (Game.BOARDSIZE - 1))
        {
            new_location = new_location - (Game.BOARDSIZE - 1);
            addMoney(200);
        }
        
        setLocation(new_location);
    }
    
    /**
     * Sets the new location of the player
     * @param location the location of the player
     */
    public void setLocation(int location)
    {
        //this keeps track of how many players are on each grid, this is needed
        //to space out the players on the GUI
        Game.board_grids[this.location].removeOccupant();
        Game.board_grids[location].addOccupant(this);
        this.location = location;
        if (Game.player_controller != null){
            Game.player_controller.updatePosition(this);
        }
    }
    
    /**
     * 
     * @param group a property group
     * @return a list of properties the player owns in that group
     */
    public List<BuyableGrid> getPropertiesOfType(BuyableGrid.PropertyGroup group)
    {
        List<BuyableGrid> of_type = new LinkedList<>();
        for (BuyableGrid grid : getProperties())
        {
            if (grid.property_group == group)
                of_type.add(grid);
        }
        return of_type;
    }
    
    /**
     * Gets the amount of get out of jail free cards the player has
     * @return the number of get out of jail free cards the player has
     */
    public int getNumberOfJailFreeCards()
    {
        return this.jail_free_cards;
    }
    
    /**
     * Sets the number of get out a jail free cards
     * @param jail_free_cards number of jail free cards
     */
    public void setNumberOfJailFreeCards(int jail_free_cards)
    {
        this.jail_free_cards = jail_free_cards;
        updateStats();
    }
    
    /**
     * Gives the player one more get out of jail free card
     */
    public void addJailFreeCard() {
        this.jail_free_cards ++;
        updateStats();
    }
    
    /**
     * Gets whether or not the player in jail
     * @return is the player in jail
     */
    public boolean isInJail()
    {
        return this.in_jail;
    }
    
    /**
     * Sets the status of whether the player is in jail
     * @param in_jail is the player in jail
     */
    public void setJailStatus(boolean in_jail)
    {
        this.in_jail = in_jail;
    }
    
    /**
     * Gets the token type of the player's token
     * @return the players token
     */
    public PlayerToken getToken()
    {
        return token;
    }
    
    /**
     * Sets the player's token
     * @param token the player wants to be
     */
    public void setToken(PlayerToken token)
    {
        this.token = token;
    }
    
    /**
     * The player buys the grid
     * @param grid the grid the player will buy
     */
    public void buyProperty(BuyableGrid grid) {
        property.add(grid);
        grid.setOwner(this);
        setMoney(this.money - grid.getPrice());
        incrementType(grid);
    }
    
    /**
     * Increments the grid's type in the hashmap for
     * the number of owned properties of that type
     * @param grid the grid of the type to increment
    */
    private void incrementType(BuyableGrid grid) {
        BuyableGrid.PropertyGroup group = grid.property_group;
        Integer count = num_of_owned_type.get(group);
        if (count == null)
        {
            count = 0;
        }
        num_of_owned_type.put(group, count+1);
    }


    /**
     * Get the number of owned properties in that grid's property group
     * @param grid the grid for which to know how many of the same type is owned
     * @return the number of the same type the player owns
     */

    public int getNumOfOwnedType(BuyableGrid grid) {
        return num_of_owned_type.get(grid.property_group);
    }
    
    /**
     * Get whether or not the player owns all properties of a group
     * @param group the group for which to know if all of the group is owned
     * @return whether the player owns all of that property group
     */
    public boolean ownsAllType(BuyableGrid.PropertyGroup group)
    {
        return num_of_owned_type.get(group) == group.MAX;
    }

    /**
     * Get whether or not the player owns all properties of a grid's group
     * @param grid the grid for which to know if all of the group is owned
     * @return whether the player owns all type of that grid
     */

    public boolean ownsAllType(BuyableGrid grid) {
        return getNumOfOwnedType(grid) == grid.property_group.MAX;
    }
    
    /**
     * Buy house on that grid
     * @param grid the grid to buy the house for
     */
    public void buyHouse(PropertyGrid grid) {
    	removeMoney(grid.getHousePrice());
    	grid.addHouse();
    }

    /**
     * Updates the player menu if the player's money, jail cards etc. has changed
     * during their turn
     */

    public void updateStats() {
        if (this == Game.current_player)
            Game.player_controller.updateStats(this);
    }
    
    /**
     * Performs the landing function for the grid the player in on
     */
    public void performLandingFunction()
    {
        getCurrentGrid().landingFunction(this);
    }
}