package monopoly;

public class Player {
    
    enum PlayerToken{DOG, BATTLESHIP, AUTOMOBILE, TOPHAT, THIMBLE, BOOT, WHEELBARROW, CAT}
    
    String name;            //What's the player's name?
    PlayerToken token;      //Which game token did the player pick?
    int money;              //How much money does the player have?
    int location;           //Which grid is the player on right now?
    int jail_free_cards;    //How many get-out-of-jail-free cards does this player have?
    
    
    /*New Player constructor*/
    public Player (String name, PlayerToken token)
    {
        this.name = name;
        this.token = token;
        this.money = 1500; 
        this.jail_free_cards = 0;
        this.location = 0;
    }
    
     /*Add more class methods below?*/
    
    public String getPlayerName()
    {
        return this.name;
    }
    
    public void setPlayerName(String name)
    {
        this.name = name;
    }
    
    public int getPlayerMoney()
    {
        return this.money;
    }
    
    public void setPlayerMoney(int money)
    {
        this.money = money;
    }
    
    public int getPlayerLocation()
    {
        return this.location;
    }
    
    public void setPlayerLocation(int location)
    {
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
    
    
}
