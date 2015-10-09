/*
    This is the main Game class. All backend game related logic should go into this file.
*/

package monopoly;
import java.util.*;

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
    
    //enum GameState {LOBBY, PLAYING, ENDED}      //do we really need this?
    
    Grid[] board_grids;                 //Array of Grid to represent a gameboard
    EventCard[] chance_cards;           //Array of EventCard that functions as a deck of chance cards
    EventCard[] comchest_cards;         //Array of EventCard that functions as a deck of community chest cards
    List<Player> player_list;           //ArrayList of Player to represent all the players of the game.
    
    /*Constructor for creating a new game*/
    public Game()
    {
        this.board_grids = new Grid[BOARDSIZE];
        player_list = new ArrayList<Player>();
        chance_cards = new EventCard[CHANCECOUNT];
        comchest_cards = new EventCard[COMCHESTCOUNT];
        initializeGame();
    }
    
    /*insert more methods below*/
    
    /*Sets up all game related tasks*/
    public void initializeGame()
    {
        initializeGrids();
        initializeChance();
        initializeComChest();
        waitForPlayers();
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
    
    /*Text based implementation of creating a list of players for the game*/
    private void waitForPlayers()
    {
       
        boolean DOG = false, BATTLESHIP= false, AUTOMOBILE = false, TOPHAT = false, THIMBLE = false, BOOT = false, WHEELBARROW = false, CAT = false;    //Keeps track of which token has been used
        Scanner scanner = new Scanner(System.in);                           
        
        //Gets new players as console input for now
        while(player_list.size() < MAXPLAYERS)
        {
            String new_player_name = null;
            PlayerToken new_player_token = null;
            
            System.out.println("========================================================================");
             System.out.println("Creating a new player for the game.");
            System.out.println("Enter 'STARTGAME' as a player's name to start game immediately");
            System.out.println("Game will also start automatically if MAXPLAYERS are met");
            System.out.println("There are currently " +player_list.size() +" players in the game. \nPlease enter the a new player's info below.");
            System.out.println("========================================================================");
            
            /*Ask for player's name*/
            System.out.println("Player name:\n");
            new_player_name = scanner.next();
            
            /*Start game??*/
            if(new_player_name.equals("STARTGAME"))
                if(player_list.size() < 1)
                {
                    System.out.println("The game requires at least 1 player to start\n");
                    continue;
                }
                else
                    break;
            
            /*Ask for a token choice*/
            System.out.println("Player Token:\n");
            System.out.println("1 = DOG\n2 = BATTLESHIP\n3 = AUTOMOBILE\n4 = TOPHAT\n5 = THIMBLE\n6 = BOOT\n7 = WHEELBARROW\n8 = CAT\n");
            
            /*Determine which token the player wanted*/
            switch(scanner.nextInt())
            {
                case(1):
                    if(DOG)
                        System.out.println("The DOG token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.DOG;
                        DOG = true;
                    }
                    break;
                case(2):
                    if(BATTLESHIP)
                        System.out.println("The BATTLESHIP token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.BATTLESHIP;
                        BATTLESHIP = true;
                    }
                    break;
                case(3):
                    if(AUTOMOBILE)
                        System.out.println("The AUTOMOBILE token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.AUTOMOBILE;
                        AUTOMOBILE = true;
                    }
                    break;
                case(4):
                    if(TOPHAT)
                        System.out.println("The TOPHAT token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.TOPHAT;
                        TOPHAT = true;
                    }
                    break;
                case(5):
                    if(THIMBLE)
                        System.out.println("The THIMBLE token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.THIMBLE;
                        THIMBLE = true;
                    }
                    break;
                case(6):
                    if(BOOT)
                        System.out.println("The BOOT token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.BOOT;
                        BOOT = true;
                    }
                    break;
                    
                case(7):
                    if(WHEELBARROW)
                        System.out.println("The WHEELBARROW token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.WHEELBARROW;
                        WHEELBARROW = true;
                    }
                    break;  
                    
                case(8):
                    if(CAT)
                        System.out.println("The CAT token has been used already!");
                    else
                    {
                        new_player_token = PlayerToken.CAT;
                        CAT = true;
                    }
                    break;
                default:
                    System.out.println("Invalid Token selection");
                    break;
            }
            
            /*Make sure the selected token was valid*/
            if(new_player_token == null)
            {
                System.out.println("Player not created!");
                continue;
            }
            
            /*Create the player object using the new data*/
            player_list.add(new Player(new_player_name, new_player_token));
            System.out.println("\nNew player created! Name: " +new_player_name +" Token: " +new_player_token.toString() +"\n");
        }
        
        /*Start the game, we have enough players*/
        startGame();
    }
    
    /*Start the actual game here*/
    private void startGame()
    {
        System.out.println("=============");
        System.out.println("GAME STARTED!");
        System.out.println("=============");
        
        /*The only point of this outer infinite loop is to reset the player iterator*/
        for(;;)
        {
            /*For each Player in player_list, where rolling_player is the current player, it's rolling_playher's turn to roll the dice*/
            for(Player rolling_player : player_list)
            {
                System.out.println("Enter ANY text (other than blank) and Player " +rolling_player.getName() +" will roll and move.");
                (new Scanner(System.in)).next();
                playerRollDiceAndMove(rolling_player);
                
            }
        }
        //System.exit(0);
    }
    
    
    /*Roll dices for a player, and move the palyer forward that many spots*/
    public int playerRollDiceAndMove(Player player)
    {
        Dice dice = new Dice();
        
        int diceroll = dice.getRoll();
        int new_location = player.getLocation() + diceroll;
        
        /*Should also add logic to get a player out of jail here*/
        
        /*Did the rolling player "overflow" and passed GO?*/
        if(new_location > (BOARDSIZE - 1))
        {
            player.setLocation(new_location - (BOARDSIZE - 1));
            player.addMoney(200);
        }
        else
            player.setLocation(new_location);
        
        
        
        System.out.println("Player " +player.getName() +" rolled " +diceroll +" and is now on grid " +player.getLocation() +" with $" +player.getMoney() +"\n");
        
        /*Return the player's new grid location*/
        return player.getLocation();
    }
    
}
