
package monopoly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    
    /*These are actions performed by the abstract player
    their implimentations differ from AI to Human
    */
    public abstract void beginTurn();
    public abstract int rollDie();          //AI automatically rolls, Human waits for the dice button
    public abstract void propertyDecision();//AI always buys, human players get a choice
    public abstract void finalizeTurn(); //any last things
    
    /*New Player constructor*/
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
    
    public Grid getCurrentGrid()
    {
        return Game.board_grids[getLocation()];
    }
    
    public void advance(int diceroll)
    {
        int new_location = getLocation() + diceroll;
        
        /*Did the rolling player "overflow" and passed GO?*/
        if(new_location > (Game.BOARDSIZE - 1))
        {
            new_location = new_location - (Game.BOARDSIZE - 1);
            addMoney(200);
        }
        
        setLocation(new_location);
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
    
    public boolean ownsAllType(BuyableGrid.PropertyGroup group)
    {
        return num_of_owned_type.get(group) == group.MAX;
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