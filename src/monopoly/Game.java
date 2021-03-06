/*
    This is the main Game class. All backend game related logic should go into this file.
 */
package monopoly;

import java.io.IOException;
import java.util.*;

/*Imports all the custom enum datatypes*/
import monopoly.BuyableGrid.PropertyGroup;
import monopoly.CardGrid.CardType;
import monopoly.GenericGrid.GenericType;
import monopoly.AbstractPlayer.PlayerToken;
import monopoly.gui.*;
import monopoly.protobuffer.*;
import monopoly.protobuffer.PlayerDataProto.PlayerList;
import monopoly.protobuffer.PlayerDataProto.Player;

/**
 * The Game of Monopoly
 *
 */
public class Game {

    /**
     * The number for each grid
     */
    public enum GRIDNUM {
        GO(0), MediterraneanAve(1), CommunityChest1(2), BalticAve(3), IncomeTax(4),
        ReadingRailroad(5), OrientalAve(6), Chance1(7), VermontAve(8), Connecticut(9),
        Jail(10), StCharlesPlace(11), ElectricCompany(12), StatesAve(13), VirginiaAve(14),
        PennsylvaniaRailroad(15), StJamesPlace(16), CommunityChest2(17), TennesseeAve(18), NewYorkAve(19),
        FreeParking(20), KentuckyAve(21), Chance2(22), IndianaAve(23), IllinoisAve(24),
        BORailroad(25), AtlanticAve(26), VentnorAve(27), WaterWorks(28), MarvinGardens(29),
        GoToJail(30), PacificAve(31), NorthCarolina(32), CommunityChest3(33), PennsylvaniaAve(34),
        ShortLineRailroad(35), Chance3(36), ParkPlace(37), LuxuryTax(38), Boardwalk(39);

        private final int num;

        GRIDNUM(final int value) {
            num = value;
        }

        /**
         * Get the number for the corresponding grid
         *
         * @return the number for the grid
         */
        public int getNum() {
            return num;
        }
    }

    final int MAXPLAYERS = 8;
    final int MINPLAYERS = 2;                   //Maximum and minimum number of players supported by the game
    int NUMPLAYERS;                             //players in the current game
    static final int BOARDSIZE = 40;           //How many grids are on the board

    public static Grid[] board_grids;                 //Array of Grid to represent a gameboard
    static List<AbstractPlayer> player_list;          //ArrayList of Player to represent all the players of the game.
    Board board;

    /**
     * A copy of two stacks representing chance and community chest decks for
     * the Game.
     */
    public static Deck deck;

    /**
     * A copy of the Dice for the game.
     */
    public static Dice dice;

    /**
     * The copy of the dice controller for the game. Used for the GUI
     */
    public static DiceController dice_controller;

    /**
     * The copy of the player controller for the game. Used for the GUI
     */
    public static PlayerMenuController menu_controller;

    /**
     * The copy of the grid controller for the game. Used for the GUI
     */
    public static GridController grid_controller;
    
    public static DialogController dialog_controller;
    
    public static ObjectLayerController object_controller;

    /**
     * The player whose turn is now
     */
    public static AbstractPlayer current_player;

    /**
     * Creates a new game
     */
    public Game() {
        Game.board_grids = new Grid[BOARDSIZE];
        player_list = new ArrayList<>();
        deck = new Deck(player_list);
        dice = new Dice();
    }

    /**
     * Sets up all game related tasks
     */
    public void initializeGame() {
        initializeGrids();

        boolean newGame = initializePlayerData();

        launchBoard();

        System.out.println("=============");
        System.out.println("GAME STARTED!");
        System.out.println("=============");

        /*Start the game, we have enough players*/
        if (newGame) {
            startGame();
        } else {
            // Refreshes positions so that all players aren't on the same grid as the first player (visual bug)
            for (AbstractPlayer p : player_list) {
                Game.object_controller.updatePlayerPosition(p);
            }
            startGame(current_player);
        }
    }

    /* Load player data (i.e. names, token, money, etc.)
     * Returns true if starting a new game, otherwise returns false if loading a saved game*/
    private boolean initializePlayerData() {
        try {
            // Attempt to load player list data
            PlayerList pl = PlayerData.loadPlayerList();

            if (pl == null) {
                // If not found then start new game
                waitForPlayers();
                return true;
            } else {
                // Otherwise, if found then ask if they want to load an old game
                System.out.println("========================================================================");
                System.out.println("You already have a saved game. Would you like to start a new game?");
                System.out.println("Enter 'NEW' to start a new game otherwise enter 'START' and the saved game will be loaded");
                System.out.println("========================================================================");

                Scanner scanner = new Scanner(System.in);
                if ((scanner.next().toLowerCase()).equals("new")) {
                        waitForPlayers();
                        scanner.close();
                        return true;
                }
                scanner.close();

                // Load players 1 by 1 into the game
                int pNum = 1;
                for (Player p: pl.getPList()) {
                    System.out.println("Loading player " + (pNum++) + " - Name: " + p.getName() + ", Token: " + p.getToken() + ", Money: " + p.getMoney() + ", Location: " + p.getLocation());

                    HumanPlayer baseHuman = new HumanPlayer();
                    AIPlayer baseAI = new AIPlayer();
                    AbstractPlayer newPlayer = null;

                    if (p.getAi()) {
                        newPlayer = baseAI.clone();
                    } else {
                        newPlayer = baseHuman.clone();
                    }

                    newPlayer.setName(p.getName());
                    newPlayer.setToken(determineToken(p.getToken()));
                    newPlayer.setMoney(p.getMoney());
                    newPlayer.setLocation(p.getLocation());
                    newPlayer.setNumberOfJailFreeCards(p.getJailFreeCards());
                    newPlayer.setJailStatus(p.getInJail());

                    if (p.getMyTurn()) {
                        current_player = newPlayer;
                    }

                    player_list.add(newPlayer);
                }

                System.out.println("Successfully loaded player data");
            }
        } catch (IOException e) {
            waitForPlayers();
            return true;
        }
        return false;
    }

    public static void saveGame() {
        try {
            PlayerData.createEmptyDataFile();
            for (AbstractPlayer p : player_list) {
                boolean myTurn = false;
                if (p == current_player) {
                    myTurn = true;
                }

                if (p instanceof HumanPlayer) {
                    PlayerData.savePlayer(p.name, p.token.ordinal(), false, p.money, p.location, p.jail_free_cards, p.isInJail(), myTurn);
                } else if (p instanceof AIPlayer) {
                    PlayerData.savePlayer(p.name, p.token.ordinal(), true, p.money, p.location, p.jail_free_cards, p.isInJail(), myTurn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Fill all grids of the board with appropriate play data*/
    private void initializeGrids() {
        /*http://ecx.images-amazon.com/images/I/81oC5pYhh2L._SL1500_.jpg
        https://en.wikibooks.org/wiki/Monopoly/Official_Rules*/
        board_grids[0] = new GenericGrid(GenericType.GO);
        board_grids[1] = new PropertyGrid(PropertyGroup.BROWN, "Mediterranean Ave", 60, 30, (new int[]{2, 10, 30, 90, 160, 250}));
        board_grids[2] = new CardGrid(CardType.COMCHEST);
        board_grids[3] = new PropertyGrid(PropertyGroup.BROWN, "Baltic Ave", 60, 30, (new int[]{4, 20, 60, 180, 320, 450}));
        board_grids[4] = new TaxGrid("Income Tax", 200);
        board_grids[5] = new RailroadGrid("Reading Railroad");
        board_grids[6] = new PropertyGrid(PropertyGroup.LIGHTBLUE, "Oriental Avenue", 100, 50, (new int[]{6, 30, 90, 270, 400, 550}));
        board_grids[7] = new CardGrid(CardType.CHANCECARD);
        board_grids[8] = new PropertyGrid(PropertyGroup.LIGHTBLUE, "Vermont Avenue", 100, 50, (new int[]{6, 30, 90, 270, 400, 550}));
        board_grids[9] = new PropertyGrid(PropertyGroup.LIGHTBLUE, "Connecticut Avenue", 120, 60, (new int[]{8, 40, 100, 300, 450, 600}));
        board_grids[10] = new GenericGrid(GenericType.JAIL);
        board_grids[11] = new PropertyGrid(PropertyGroup.MAGENTA, "St. Charles Place", 140, 70, (new int[]{10, 50, 150, 450, 625, 750}));
        board_grids[12] = new UtilityGrid("Electric Company");
        board_grids[13] = new PropertyGrid(PropertyGroup.MAGENTA, "States Avenue", 140, 70, (new int[]{10, 50, 150, 450, 625, 750}));
        board_grids[14] = new PropertyGrid(PropertyGroup.MAGENTA, "Virginia Avenue", 160, 80, (new int[]{12, 60, 180, 500, 700, 900}));
        board_grids[15] = new RailroadGrid("Pennsylvania Railroad");
        board_grids[16] = new PropertyGrid(PropertyGroup.ORANGE, "St. James Place", 180, 90, (new int[]{14, 70, 200, 550, 750, 950}));
        board_grids[17] = new CardGrid(CardType.COMCHEST);
        board_grids[18] = new PropertyGrid(PropertyGroup.ORANGE, "Tennessee Avenue", 180, 90, (new int[]{14, 70, 200, 550, 750, 950}));
        board_grids[19] = new PropertyGrid(PropertyGroup.ORANGE, "New York Avenue", 200, 100, (new int[]{16, 80, 220, 600, 800, 1000}));
        board_grids[20] = new GenericGrid(GenericType.FREEPARKING);
        board_grids[21] = new PropertyGrid(PropertyGroup.RED, "Kentucky Avenue", 220, 110, (new int[]{18, 90, 250, 700, 875, 1050}));
        board_grids[22] = new CardGrid(CardType.CHANCECARD);
        board_grids[23] = new PropertyGrid(PropertyGroup.RED, "Indiana Avenue", 220, 110, (new int[]{18, 90, 250, 700, 875, 1050}));
        board_grids[24] = new PropertyGrid(PropertyGroup.RED, "Illinois Ave", 240, 120, (new int[]{20, 100, 300, 750, 925, 1100}));
        board_grids[25] = new RailroadGrid("B & O Railroad");
        board_grids[26] = new PropertyGrid(PropertyGroup.YELLOW, "Atlantic Avenue", 260, 130, (new int[]{22, 110, 330, 800, 975, 1150}));
        board_grids[27] = new PropertyGrid(PropertyGroup.YELLOW, "Ventnor Avenue", 260, 130, (new int[]{22, 110, 330, 800, 975, 1150}));
        board_grids[28] = new UtilityGrid("Water Works");
        board_grids[29] = new PropertyGrid(PropertyGroup.YELLOW, "Marvin Gardens", 280, 140, (new int[]{24, 120, 360, 850, 1025, 1200}));
        board_grids[30] = new GenericGrid(GenericType.GOTOJAIL);
        board_grids[31] = new PropertyGrid(PropertyGroup.GREEN, "Pacific Avenue", 300, 150, (new int[]{26, 130, 390, 900, 1100, 1275}));
        board_grids[32] = new PropertyGrid(PropertyGroup.GREEN, "North Carolina Avenue", 300, 150, (new int[]{26, 130, 390, 900, 1100, 1275}));
        board_grids[33] = new CardGrid(CardType.COMCHEST);
        board_grids[34] = new PropertyGrid(PropertyGroup.GREEN, "Pennsylvania Avenue", 320, 160, (new int[]{28, 150, 450, 1000, 1200, 1400}));
        board_grids[35] = new RailroadGrid("Short Line Railroad");
        board_grids[36] = new CardGrid(CardType.CHANCECARD);
        board_grids[37] = new PropertyGrid(PropertyGroup.BLUE, "Park Place", 350, 175, (new int[]{35, 175, 500, 1100, 1300, 1500}));
        board_grids[38] = new TaxGrid("Luxury Tax", 75);
        board_grids[39] = new PropertyGrid(PropertyGroup.BLUE, "Boardwalk", 400, 200, (new int[]{50, 200, 600, 1400, 1700, 2000}));

    }

    /*Text based implementation of creating a list of players for the game*/
    private void waitForPlayers() {
        HumanPlayer baseHuman = new HumanPlayer();
        AIPlayer baseAI = new AIPlayer();
        baseHuman.setMoney(1500);
        baseHuman.setLocation(0);
        baseAI.setMoney(1500);
        baseAI.setLocation(0);
        boolean DOG = false, BATTLESHIP = false, AUTOMOBILE = false, TOPHAT = false, THIMBLE = false, BOOT = false, WHEELBARROW = false, CAT = false;    //Keeps track of which token has been used
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================================================");
        System.out.println("How many players will play in this game?");
        System.out.println("Please choose between 2 and 8");
        System.out.println("========================================================================");

        NUMPLAYERS = scanner.nextInt();
        scanner.nextLine();

        if (NUMPLAYERS < MINPLAYERS || NUMPLAYERS > MAXPLAYERS) {
            System.out.println("Invalid number of players");
            System.exit(-1);
        }
        //Gets new players as console input for now
        while (player_list.size() < NUMPLAYERS) {
            String new_player_name = null;
            PlayerToken new_player_token = null;

            System.out.println("========================================================================");
            System.out.println("Creating a new human player for the game.");
            System.out.println("Enter 'DONE' as a player's name to make the remaining " + (NUMPLAYERS - player_list.size()) + " into AI's");
            System.out.println("Game will also start automatically if " + NUMPLAYERS + " are created");
            System.out.println("There are currently " + player_list.size() + " players in the game. \nPlease enter the a new player's info below.");
            System.out.println("========================================================================");

            /*Ask for player's name*/
            System.out.println("Player name:\n");
            new_player_name = scanner.next();

            /*Start game??*/
            if (new_player_name.equals("DONE")) {
                break;
            }

            /*Ask for a token choice*/
            System.out.println("Player Token:\n");
            System.out.println("1 = DOG\n2 = BATTLESHIP\n3 = AUTOMOBILE\n4 = TOPHAT\n5 = THIMBLE\n6 = BOOT\n7 = WHEELBARROW\n8 = CAT\n");

            /*Determine which token the player wanted*/
            switch (scanner.nextInt()) {
                case (1):
                    if (DOG) {
                        System.out.println("The DOG token has been used already!");
                    } else {
                        new_player_token = PlayerToken.DOG;
                        DOG = true;
                    }
                    break;
                case (2):
                    if (BATTLESHIP) {
                        System.out.println("The BATTLESHIP token has been used already!");
                    } else {
                        new_player_token = PlayerToken.BATTLESHIP;
                        BATTLESHIP = true;
                    }
                    break;
                case (3):
                    if (AUTOMOBILE) {
                        System.out.println("The AUTOMOBILE token has been used already!");
                    } else {
                        new_player_token = PlayerToken.AUTOMOBILE;
                        AUTOMOBILE = true;
                    }
                    break;
                case (4):
                    if (TOPHAT) {
                        System.out.println("The TOPHAT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.TOPHAT;
                        TOPHAT = true;
                    }
                    break;
                case (5):
                    if (THIMBLE) {
                        System.out.println("The THIMBLE token has been used already!");
                    } else {
                        new_player_token = PlayerToken.THIMBLE;
                        THIMBLE = true;
                    }
                    break;
                case (6):
                    if (BOOT) {
                        System.out.println("The BOOT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.BOOT;
                        BOOT = true;
                    }
                    break;

                case (7):
                    if (WHEELBARROW) {
                        System.out.println("The WHEELBARROW token has been used already!");
                    } else {
                        new_player_token = PlayerToken.WHEELBARROW;
                        WHEELBARROW = true;
                    }
                    break;

                case (8):
                    if (CAT) {
                        System.out.println("The CAT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.CAT;
                        CAT = true;
                    }
                    break;
                default:
                    System.out.println("Invalid Token selection");
                    break;
            }

            /*Make sure the selected token was valid*/
            if (new_player_token == null) {
                System.out.println("Player not created!");
                continue;
            }

            /*Create the player object using the new data*/
            AbstractPlayer newPlayer = baseHuman.clone();
            newPlayer.setName(new_player_name);
            newPlayer.setToken(new_player_token);
            player_list.add(newPlayer);
            System.out.println("\nNew player created! Name: " + newPlayer.getName() + " Token: " + newPlayer.getToken().toString() + "\n");
        }

        while (player_list.size() < NUMPLAYERS) {
            String new_player_name = null;
            PlayerToken new_player_token = null;

            System.out.println("========================================================================");
            System.out.println("Creating a new AI player for the game.");
            System.out.println("Game will also start automatically if " + NUMPLAYERS + " are created");
            System.out.println("There are currently " + player_list.size() + " players in the game. \nPlease enter the a new player's info below.");
            System.out.println("========================================================================");

            /*Ask for player's name*/
            System.out.println("Player name:\n");
            new_player_name = scanner.next();

            /*Start game??*/
            if (new_player_name.equals("DONE")) {
                break;
            }

            /*Ask for a token choice*/
            System.out.println("Player Token:\n");
            System.out.println("1 = DOG\n2 = BATTLESHIP\n3 = AUTOMOBILE\n4 = TOPHAT\n5 = THIMBLE\n6 = BOOT\n7 = WHEELBARROW\n8 = CAT\n");

            /*Determine which token the player wanted*/
            switch (scanner.nextInt()) {
                case (1):
                    if (DOG) {
                        System.out.println("The DOG token has been used already!");
                    } else {
                        new_player_token = PlayerToken.DOG;
                        DOG = true;
                    }
                    break;
                case (2):
                    if (BATTLESHIP) {
                        System.out.println("The BATTLESHIP token has been used already!");
                    } else {
                        new_player_token = PlayerToken.BATTLESHIP;
                        BATTLESHIP = true;
                    }
                    break;
                case (3):
                    if (AUTOMOBILE) {
                        System.out.println("The AUTOMOBILE token has been used already!");
                    } else {
                        new_player_token = PlayerToken.AUTOMOBILE;
                        AUTOMOBILE = true;
                    }
                    break;
                case (4):
                    if (TOPHAT) {
                        System.out.println("The TOPHAT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.TOPHAT;
                        TOPHAT = true;
                    }
                    break;
                case (5):
                    if (THIMBLE) {
                        System.out.println("The THIMBLE token has been used already!");
                    } else {
                        new_player_token = PlayerToken.THIMBLE;
                        THIMBLE = true;
                    }
                    break;
                case (6):
                    if (BOOT) {
                        System.out.println("The BOOT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.BOOT;
                        BOOT = true;
                    }
                    break;

                case (7):
                    if (WHEELBARROW) {
                        System.out.println("The WHEELBARROW token has been used already!");
                    } else {
                        new_player_token = PlayerToken.WHEELBARROW;
                        WHEELBARROW = true;
                    }
                    break;

                case (8):
                    if (CAT) {
                        System.out.println("The CAT token has been used already!");
                    } else {
                        new_player_token = PlayerToken.CAT;
                        CAT = true;
                    }
                    break;
                default:
                    System.out.println("Invalid Token selection");
                    break;
            }

            /*Make sure the selected token was valid*/
            if (new_player_token == null) {
                System.out.println("Player not created!");
                continue;
            }

            /*Create the player object using the new data*/
            AbstractPlayer newPlayer = baseAI.clone();
            newPlayer.setName(new_player_name);
            newPlayer.setToken(new_player_token);
            player_list.add(newPlayer);
            System.out.println("\nNew player created! Name: " + newPlayer.getName() + " Token: " + newPlayer.getToken().toString() + "\n");
        }
    }

    private PlayerToken determineToken(int tokenNum) {
        switch (tokenNum) {
            case (1):
                return PlayerToken.DOG;
            case (2):
                return PlayerToken.BATTLESHIP;
            case (3):
                return PlayerToken.AUTOMOBILE;
            case (4):
                return PlayerToken.TOPHAT;
            case (5):
                return PlayerToken.THIMBLE;
            case (6):
                return PlayerToken.BOOT;
            case (7):
                return PlayerToken.WHEELBARROW;
            case (8):
                return PlayerToken.CAT;
            default:
                System.out.println("Invalid Token loaded");
                System.exit(-1);
        }
        return null;
    }

    /* Starts the game with player p taking the first turn */
    private void startGame(AbstractPlayer p) {
        int playerNum = 0;
        for (int i = 0; i < player_list.size(); i++) {
            AbstractPlayer rolling_player = player_list.get(i);
            if (p == rolling_player) {
                playerNum = i;
                break;
            }
        }

        /*It's rolling_player's turn to roll the dice*/
        for (int i = playerNum; i < player_list.size(); i++) {
            AbstractPlayer rolling_player = player_list.get(i);
            playerRollDiceAndMove(rolling_player);
        }

        startGame();    // Continue playing game starting with the first player
    }

    /*Start the actual game here*/
    private void startGame() {
        /*The only point of this outer infinite loop is to reset the player iterator*/
        for(;;)
        {
            /*For each Player in player_list, where rolling_player is the current player, it's rolling_playher's turn to roll the dice*/
            for(AbstractPlayer rolling_player : player_list)
            {
                playerRollDiceAndMove(rolling_player);

            }
        }
        //System.exit(0);
    }

    private void launchBoard() {
        Game.object_controller = new ObjectLayerController(player_list);
        Game.menu_controller = new PlayerMenuController();
        Game.dice_controller = new DiceController(dice);
        Game.dialog_controller = new DialogController();
        Game.grid_controller = new GridController(board_grids);
        this.board = new Board();
        board.run();
    }

    /**
     * Implements the player's turn. Roll dices for a player, and moves the
     * player forward that many spots, performs the appropriate action for the
     * grid landed on
     *
     * @param player the current player's turn
     * @return the player's new location
     */
    public int playerRollDiceAndMove(AbstractPlayer player) {
        System.out.println("It is now " + player.getName() + "'s turn to roll");

        player.takeTurn();

        System.out.println("Player " + player.getName() + " rolled " + dice.getRoll() + " and is now on grid " + player.getLocation() + " with $" + player.getMoney() + "\n");

        /*Return the player's new grid location*/
        return player.getLocation();
    }

}
