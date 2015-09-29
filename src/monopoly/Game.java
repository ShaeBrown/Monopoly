/*
    This is the main Game class. All backend game related logic should go into this file.
*/

package monopoly;

/*Imports all the custom enum datatypes*/
import monopoly.Grid.GridType;                          
import monopoly.BuyablePropertyClass.PropertyGroup;
import monopoly.Player.PlayerToken;
import monopoly.EventCard.ActionType;

public class Game {
    
    final int MAXPLAYERS = 8;           //Maximum number of players supported by the game
    final int BOARDSIZE = 40;           //How many grids are on the board
    final int CHANCECOUNT = 16;         //How many chance cards are in the game
    final int COMCHESTCOUNT = 17;       //How many community chest cards are in the game
    
    Grid[] board_grids;                 //Array of Grid to represent a gameboard
    Player[] player_list;               //Array of Player to represent all the players of the game. Maybe we should switch to a dynamic type like ArrayList instead??
    EventCard[] chance_cards;           //Array of EventCard that functions as a deck of chance cards
    EventCard[] comchest_cards;         //Array of EventCard that functions as a deck of community chest cards
    
    /*Constructor for creating a new game*/
    public Game()
    {
        this.board_grids = new Grid[BOARDSIZE];
        player_list = new Player[MAXPLAYERS];
        chance_cards = new EventCard[CHANCECOUNT];
        comchest_cards = new EventCard[COMCHESTCOUNT];
        initializeGame();
    }
    
    /*Constructor for creating a new game with a player list*/
    public Game(Player[] player_list)
    {
        this.board_grids = new Grid[40];
        this.player_list = player_list;
        chance_cards = new EventCard[CHANCECOUNT];
        comchest_cards = new EventCard[COMCHESTCOUNT];
        initializeGame();
    }
    
    /*insert more methods below*/
    
    /*Sets up all game related tasks*/
    private void initializeGame()
    {
        initializeGrids();
        initializeChance();
        initializeComChest();
    }
    
    /*Fill all grids of the board with appropriate play data*/
    private void initializeGrids()
    {
        /*http://ecx.images-amazon.com/images/I/81oC5pYhh2L._SL1500_.jpg*/
        
        board_grids[0] = new Grid(GridType.GENERIC, "GO");
        board_grids[1] = new Grid(new PropertyGrid(PropertyGroup.BROWN, "Mediterranean Ave", 60, 30, 50, (new int[] {10, 30, 90, 160, 250})));
        board_grids[2] = new Grid(GridType.COMCHEST);
        board_grids[3] = new Grid(new PropertyGrid(PropertyGroup.BROWN, "Baltic Ave", 60, 30, 50, (new int[] {20, 60, 180, 320, 450})));
        board_grids[4] = new Grid(new TaxGrid("Income Tax", 200));
        board_grids[5] = new Grid(new RailroadGrid("Reading Railroad", 200, 100));
        /*fill more...*/
        board_grids[7] = new Grid(GridType.CHANCE);
        /*fill more...*/
        board_grids[10] = new Grid(GridType.GENERIC, "IN JAIL/JUST VISITING");
        /*fill more...*/
        board_grids[12] = new Grid(new UtilityGrid("Electric Company", 150, 75));
        /*fill more...*/
        board_grids[20] = new Grid(GridType.GENERIC, "Free Parking");
        /*fill more...*/
        board_grids[30] = new Grid(GridType.GOTOJAIL);
        /*fill more...*/
    }
    
    private void initializeChance()
    {
        /*http://monopoly.wikia.com/wiki/Chance*/
        
        chance_cards[0] = new EventCard("Advance to Go", "Collect $200" ,ActionType.MOVETO, 0);
        /*fill more...*/
        
    }
    
    private void initializeComChest()
    {
        /*http://monopoly.wikia.com/wiki/Community_Chest*/
        
        comchest_cards[0] = new EventCard("Advance to Go", "Collect $200" ,ActionType.MOVETO, 0);
        /*fill more...*/
    }
    
    private void addPlayer(Player player)
    {
        
    }
    
    
    
}
