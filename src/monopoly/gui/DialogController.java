/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import monopoly.AbstractPlayer;
import monopoly.BuyableGrid;
import monopoly.CardGrid;
import monopoly.Game;
import monopoly.GenericGrid;
import monopoly.PropertyGrid;
import monopoly.TradeRequest;

/**
 * Controller for all dialogs for player decisions and notifications
 *
 * 
 */
public class DialogController {

    ImageIcon moneybags; // image for the chance/community cards.
    ImageIcon arrow;   //image for transfer
    List<BuyableGrid> trade; //selected properties to trade
    
    /**
     * Creates a new Dialog Controller
     */
    public DialogController() {
        
        this.moneybags = new ImageIcon(getClass().getResource("/monopoly/gui/img/moneybags.png"));
        this.arrow = new ImageIcon(getClass().getResource("/monopoly/gui/img/arrow.png"));
    }

    //TODO(nick): Implement logic that shows buy hotel when the property has 4 hotels
    /* Creates a dialogue asking if the player wants to buy a house */
    /**
     * Opens a dialog to confirm a house purchase
     */
    public void buyHouse() {

        PropertyGrid g = Game.menu_controller.selectedProperty;
        AbstractPlayer p = g.getOwner();

        ImageIcon icon = Game.grid_controller.getPropertyCard(g);
        String msg = "Would you like to buy a house for " + g.getName() + " for $" + g.getHousePrice() + "?\n";
        msg += "You currently have " + g.getCurrentHouses();
        if (g.getCurrentHouses() == 1) {
            msg += " house.";
        } else {
            msg += " houses.";
        }

        int buy = JOptionPane.showConfirmDialog(null, msg, "Buy House", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

        // tmp - can remove once I've implemented logic to only display button when necessary
        if (g.getOwner() == null) {
            return;
        }

        if (buy == JOptionPane.YES_OPTION) {
            if (p.getMoney() < g.getHousePrice()) {
                // Not enough money to buy house
            } else if (g.getCurrentHouses() < g.MAX_NUMBER_HOUSES) {
                p.buyHouse(g);
                Game.menu_controller.setProperty(g);
            }
        }
    }
    
    /**
     * Opens a dialog asking if the player wants to buy the BuyableGrid they
     * just landed on
     * @param p the player with the choice
     */
    public void buyProperty(AbstractPlayer p) {
        assert (Game.grid_controller.getGrid(p.getLocation()) instanceof BuyableGrid);
        BuyableGrid g = (BuyableGrid) Game.grid_controller.getGrid(p.getLocation());
        ImageIcon icon = Game.grid_controller.getPropertyCard(g);
        int buy = JOptionPane.showConfirmDialog(null, "Would you like to buy " + g.getName() + " for $" + g.getPrice() + "?", "Buy Property", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        if (buy == JOptionPane.YES_OPTION) {
            if (p.getMoney() < g.getPrice()) {

            } else {
                p.buyProperty(g);
            }
        } else {
            //auction or nothing?
        }
    }
    
        /**
     * Displays the drawn chance or community card
     *
     * @param type type of card
     * @param card the description of the card
     */
    public void displayCard(CardGrid.CardType type, String card) {
        if (type == CardGrid.CardType.CHANCECARD) {
            JOptionPane.showMessageDialog(null, card, "CHANCE", JOptionPane.PLAIN_MESSAGE, moneybags);
        } else {
            JOptionPane.showMessageDialog(null, card, "COMMUNITY CHEST", JOptionPane.PLAIN_MESSAGE, moneybags);
        }
    }
    
     /**
     * Opens a dialog to notify players of money transfer when rent is payed
     * @param owner the owner of the property
     * @param landed the player of landed on it
     * @param grid the grid landed on
     */
    public void displayRent(AbstractPlayer owner, AbstractPlayer landed, BuyableGrid grid) {
        JPanel rentImages = new JPanel();
        rentImages.add(new JLabel(Game.object_controller.getPlayerIcon(landed)));
        rentImages.add(new JLabel(arrow));
        rentImages.add(new JLabel(Game.object_controller.getPlayerIcon(owner)));
        String msg = "You must pay " + owner.getName() + " $" + grid.getRentPrice() + " for landing on " + grid.getName();
        rentImages.add(new JLabel(msg));
        JOptionPane.showMessageDialog(null, rentImages, "PAY RENT", JOptionPane.PLAIN_MESSAGE);
    }
    
     /**
     * Display a message to the player
     * @param msg the msg to show
     */
    public void displayMessage(String msg)
    {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    /**
     * Prompts the player to choose how they want to leave jail.
     * Use Jail Free Card, pay bail, or roll for doubles
     * @param p the player is jail
     * @return can the player leave jail
     */
    public boolean displayJailChoice(AbstractPlayer p)
    {
        //
        Object[] options;
        int type_option;
        if (p.getNumberOfJailFreeCards() == 0) {
            options = new String[]{"Pay Bail", "Try to roll doubles"};
            type_option = JOptionPane.YES_NO_OPTION;
        }
        else {
            options = new String[]{ //YES
                "Pay Bail", //NO
                "Try to roll doubles",
                "Use Jail Free Card"}; //CANCEL
            type_option = JOptionPane.YES_NO_CANCEL_OPTION;
        }
        int answer = JOptionPane.showOptionDialog(null,
        "How will you get out of jail?",
        "You are in Jail",
        type_option,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);
        switch (answer) {
        //Pay Bail
            case JOptionPane.YES_OPTION:
                p.removeMoney(50);
                GenericGrid freeparking = (GenericGrid)Game.board_grids[Game.GRIDNUM.FreeParking.getNum()];
                freeparking.addToJackPot(50);
                return true;
        //Roll doubles
            case JOptionPane.NO_OPTION:
                p.rollDie();
                if (Game.dice.isDoubles()) {
                    Game.dice_controller.enable();
                    return true;
                }
                else
                    return false;
        //Use jail free card
            case JOptionPane.CANCEL_OPTION:
                p.useJailFreeCard();
                return true;
            default:
                //Something went wrong
                return false;
        }
    }
    
    /**
     * Opens a dialog for a player to open a trade request.
     * Can choose to offer money and/or multiple properties
     * @param requester the player making the request
     * @param requestee the player the trade is directed too
     * @param requested the BuyableGrid the requester wants
     */
    public void createTradeRequest(AbstractPlayer requester, AbstractPlayer requestee, BuyableGrid requested)
    {
        JPanel choices = new JPanel();
        
        choices.setPreferredSize(new Dimension(300, 300));
        choices.setLayout(new FlowLayout());
        choices.add(new JLabel("Make an offer for " + requestee.getName() + "'s " + requested + "\n"));
        
        choices.add(new JLabel("Choose properties to trade"));
        DefaultListModel property_list = new DefaultListModel();
        
        for (BuyableGrid g : requester.getProperties()) {
            property_list.addElement(g);
        }
        
        JList properties = new JList(property_list);
        properties.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                if (list.getSelectedIndex() != -1) {
                    List<BuyableGrid> selected = list.getSelectedValuesList();
                    Game.dialog_controller.trade = selected;
                }
                else
                {
                    Game.dialog_controller.trade = null;
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(properties);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        choices.add(scrollPane);
        
        choices.add(new JLabel("Optionally choose to add money to the offer\n"));
        
        JTextField money_input = new JTextField(3);
        
        
        choices.add(money_input);

        int choice = JOptionPane.showConfirmDialog(null, choices, "Trade", JOptionPane.OK_CANCEL_OPTION);
        
        int money;
        
        try {
            money = Integer.parseInt(money_input.getText());
        }
        catch(NumberFormatException e) {
            money = 0;
        }
        
        if (choice == JOptionPane.OK_OPTION)
        {
            TradeRequest request = new TradeRequest(requester, requested, trade, money);
            request.send();
        }
    }
    
    /**
     * Opens a dialog for a player to accept or decline a trade
     * @param request the trade request object
     */
    public void openTradeRequest(TradeRequest request)
    {

        AbstractPlayer requester = request.getRequester();
        List<BuyableGrid> offer = request.getPropertyOffer();
        int money_offer = request.getMoneyOffer();
        BuyableGrid requested = request.getRequestedProperty();
        
        JPanel trade = new JPanel();
        trade.add(new JLabel(Game.object_controller.getPlayerIcon(requester)));
        trade.add(new JLabel(arrow));
        trade.add(new JLabel(Game.object_controller.getPlayerIcon(Game.current_player)));
        trade.setSize(300, 300);
        
        String money = "";
        String properties = "";
        
        if (money_offer > 0)
            money = ( offer!=null && offer.size() > 0  ? " and $" + money_offer : "$"+ money_offer);
        
        //if the trade is only money
        if (offer != null) {
            for (BuyableGrid grid : offer)
            {
            if (offer.get(offer.size()-1) != grid)
                properties += grid + ", ";
            else
                properties += (offer.size() > 1 ? "and " + grid : grid);
            }
        }
        
        
        String text_offer = requester.getName() + " is wanting to trade " + properties + money + " for your " + requested;
        trade.add(new JLabel(text_offer));
        
        int choice = JOptionPane.showConfirmDialog(null, trade , "Trade", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
            request.accept();
        else if (choice == JOptionPane.NO_OPTION)
            request.decline();
    }
    
}
