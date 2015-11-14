
package monopoly;

import java.util.HashMap;
import java.util.LinkedList;
/**
 *
 * @author Jacob Hodge
 * abstract player class which should be used to create all other players
 */
public abstract class AbstractPlayer {
    
    enum PlayerToken{DOG, BATTLESHIP, AUTOMOBILE, TOPHAT, THIMBLE, BOOT, WHEELBARROW, CAT}
    
    protected String name;            //What's the player's name?
    protected PlayerToken token;      //Which game token did the player pick?
    /* These are private because when they are changed, the player controller must change the menu
    and the tokens on the board as well
    */
    protected int money;              //How much money does the player have?
    protected int location;           //Which grid is the player on right now?
    protected int jail_free_cards;    //How many get-out-of-jail-free cards does this player have?
    protected boolean in_jail;       //Is player in jail?
    protected LinkedList<BuyableGrid> property;
    protected HashMap<BuyableGrid.PropertyGroup, Integer> num_of_owned_type;
    
    
    /*New Player constructor*/
    public AbstractPlayer ()
    {
        this.name = "";
        this.token = PlayerToken.DOG;
        this.money = 0;
        this.location = 0;
        this.jail_free_cards = 0;
        this.in_jail = false;
        this.property = new LinkedList<BuyableGrid>();
        Game.board_grids[0].addOccupant(this);
        num_of_owned_type = new HashMap<BuyableGrid.PropertyGroup, Integer>();
    }
    
    //clones the current instance of the AbstractPlayer and returns the new object
    public abstract AbstractPlayer clone();
    
     /*Add more class methods below?*/
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getMoney()
    {
        return this.money;
    }
    
    public LinkedList<BuyableGrid> getProperties() 
    {
        return property;
    }
    
    public void setProperties(LinkedList<BuyableGrid> properties)
    {
        this.property = properties;
    }
    
    public void setMoney(int money)
    {
        this.money = money;
        updateStats();
    }
    
    public void addMoney(int money)
    {
        this.money += money;
        updateStats();
    }
    
    public void removeMoney(int money) {
        this.money -= money;
        updateStats();
    }
    
    public int getLocation()
    {
        return this.location;
    }
    
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
    
    public int getNumberOfJailFreeCards()
    {
        return this.jail_free_cards;
    }
    
    public void setNumberOfJailFreeCards(int jail_free_cards)
    {
        this.jail_free_cards = jail_free_cards;
        updateStats();
    }
    
    public void addJailFreeCard() {
        this.jail_free_cards ++;
        updateStats();
    }
    
    public boolean isInJail()
    {
        return this.in_jail;
    }
    
    public void setJailStatus(boolean in_jail)
    {
        this.in_jail = in_jail;
    }
    
    public PlayerToken getToken()
    {
        return token;
    }
    
    public void setToken(PlayerToken t)
    {
        this.token = t;
    }
    
    
    public void buyProperty(BuyableGrid grid) {
        property.add(grid);
        grid.setOwner(this);
        setMoney(this.money - grid.getPrice());
        incrementType(grid);
    }
    
    /* increments the grid's type in the hashmap for
    the number of owned properties of that type
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
    
    
    /* returns the number of the grid's type the player owns*/
    public int getNumOfOwnedType(BuyableGrid grid) {
        return num_of_owned_type.get(grid.property_group);
    }
    
    /* returns whether or not the player ownes all of the grid's type */
    public boolean ownsAllType(BuyableGrid grid) {
        return getNumOfOwnedType(grid) == grid.property_group.MAX;
    }
    
    public void buyHouse(PropertyGrid grid) {
    	removeMoney(grid.getHousePrice());
    	grid.addHouse();
    }
    
    /* Changes the menu if the player gained/lost money or get out of jail free
    card during their turn */
    public void updateStats() {
        if (this == Game.current_player)
            Game.player_controller.updateStats(this);
    }
}