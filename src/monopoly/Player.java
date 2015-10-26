package monopoly;

public class Player {
    
    enum PlayerToken{DOG, BATTLESHIP, AUTOMOBILE, TOPHAT, THIMBLE, BOOT, WHEELBARROW, CAT}
    
    String name;            //What's the player's name?
    PlayerToken token;      //Which game token did the player pick?
    int money;              //How much money does the player have?
    int location;           //Which grid is the player on right now?
    int jail_free_cards;    //How many get-out-of-jail-free cards does this player have?
    boolean in_jail;       //Is player in jail?
   
    
    
    /*New Player constructor*/
    public Player (String name, PlayerToken token)
    {
        this.name = name;
        this.token = token;
        this.money = 1500; 
        this.jail_free_cards = 0;
        this.location = 0;
        Game.board_grids[0].addOccupant(this);
        this.in_jail = false;
    }
    
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
    
    public void setMoney(int money)
    {
        this.money = money;
    }
    
    public void addMoney(int money)
    {
        this.money += money;
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
    }
    
    public int getNumberOfJailFreeCards()
    {
        return this.jail_free_cards;
    }
    
    public void setNumberOfJailFreeCards(int jail_free_cards)
    {
        this.jail_free_cards = jail_free_cards;
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
    
}
