/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    BuyableGrid mortgage;
    
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
        boolean house = g.getCurrentHouses() < g.MAX_NUMBER_HOUSES;
        ImageIcon icon = Game.grid_controller.getPropertyCard(g);
        
        String msg = "Would you like to buy a " + (house ? "house" : "hotel") + " for " + g.getName() + " for $" + g.getHousePrice() + "?\n";
        
        if (house)
        {
            msg += "You currently have " + g.getCurrentHouses();
            if (g.getCurrentHouses() == 1) {
                msg += " house.";
            } else {
                msg += " houses.";
            }
        }

        int buy = JOptionPane.showConfirmDialog(null, msg, "Buy " + (house ? "House" : "Hotel") , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);


        if (buy == JOptionPane.YES_OPTION) {
            if (p.getMoney() < g.getHousePrice()) {
                displayMessage("You only have $" + p.getMoney() + ". You need $" + g.getHousePrice() + " to buy a " + (house ? "house." : "hotel."));
            } else {
                p.buyHouseorHotel(g);
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
        
        choices.setPreferredSize(new Dimension(600, 400));

        JLabel title =  new JLabel("<html>Make an offer for " + requestee.getName() + "'s " + requested + "<br><br>Choose properties to trade</html>");
        
        choices.add(title);

        
        JLabel property = new JLabel();
        property.setSize(250, 290);
        
        

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
                    property.setIcon(Game.grid_controller.getPropertyCard((BuyableGrid)list.getSelectedValue()));
                }
                else
                {
                    Game.dialog_controller.trade = null;
                    property.setIcon(null);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(properties);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        JPanel list = new JPanel();
        list.add(property);
        list.setPreferredSize(new Dimension(600,300));


        list.add(scrollPane);
        
        choices.add(list);
        
        choices.add(new JLabel("Optionally choose to add money to the offer"));
        
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
        ImageIcon icon = Game.grid_controller.getPropertyCard(requested);
        
        int choice = JOptionPane.showConfirmDialog(null, text_offer,"Trade Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        if (choice == JOptionPane.YES_OPTION)
            request.accept();
        else if (choice == JOptionPane.NO_OPTION)
            request.decline();
    }
    
    public void mortgage(AbstractPlayer broke)
    {
        JPanel popup = new JPanel();
        popup.setSize(600,400);
        
        JPanel property_chooser = new JPanel();
        DefaultListModel property_list = new DefaultListModel();
        
        JButton mortgage_property = new JButton();
        JPanel houses = new JPanel();
        houses.setSize(250, 30);
        
        JButton mortgage_house = new JButton();
        mortgage_house.setVisible(false);
        mortgage_house.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyGrid property = (PropertyGrid) mortgage;
                houses.remove(0);
                houses.repaint();
                houses.revalidate();
                /*
                Add logic to mortgage a house, or hotel
                */
                if (property.getCurrentHouses() == 0 && property.getCurrentHotels() == 0)
                {
                    mortgage_house.setVisible(false);
                    mortgage_property.setVisible(true);
                }
            }
            
        });
        
        mortgage_property.setText("Mortgage property");
        mortgage_property.setVisible(false);
        mortgage_property.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                Add logic to mortgage a house, or hotel
                */
                 SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    property_list.removeElement(mortgage);
                }
            });
                }
            
        });
        
        JButton retire = new JButton();
        retire.setText("Retire");
        retire.setVisible(false);
        retire.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               //retire from game
            }
            
        });
        
        property_chooser.setPreferredSize(new Dimension(600,300));

        JLabel property = new JLabel();
        
        for (BuyableGrid g : broke.getProperties()) {
            property_list.addElement(g);
        }
        
        JList properties = new JList(property_list);
        properties.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                if (list.getSelectedIndex() != -1) {
                    BuyableGrid selected = (BuyableGrid) list.getSelectedValue();
                    houses.removeAll();
                    mortgage = selected;
                    property.setIcon(Game.grid_controller.getPropertyCard(selected));
                    if (selected instanceof PropertyGrid)
                    {
                        PropertyGrid property = (PropertyGrid) selected;
                        if (property.getCurrentHouses() > 0 || property.getCurrentHotels() > 0)
                        {
                            mortgage_house.setVisible(true);
                            mortgage_house.setText( property.getCurrentHotels() > 0 ? "Mortgage hotel" : "Mortgage house");
                            mortgage_property.setVisible(false);
                            for (int i = 0 ; i < property.getCurrentHouses(); i++)
                            {
                                JLabel icon = new JLabel();
                                icon.setIcon(Game.object_controller.house_icon);
                                icon.setVisible(true);
                                houses.add(icon);
                            }
                            for (int i = 0 ; i < property.getCurrentHotels(); i ++)
                            {
                                JLabel icon = new JLabel();
                                icon.setIcon(Game.object_controller.hotel_icon);
                                icon.setVisible(true);
                                houses.add(icon);
                            }
                            property_chooser.repaint();
                            property_chooser.revalidate();
                            
                        }
                        else
                        {
                            mortgage_house.setVisible(false);
                            mortgage_property.setVisible(true);
                        }
                    }
                    else
                    {
                        mortgage_house.setVisible(false);
                        mortgage_property.setVisible(true);
                    }
                    
                }
                else
                {
                    Game.dialog_controller.trade = null;
                    property.setIcon(null);
                    mortgage_house.setVisible(false);
                    mortgage_property.setVisible(false);
                    if (broke.getMoney() < 0 && broke.getProperties().isEmpty())
                        retire.setVisible(true);
                    
                }
            }
            
        });
        
        
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(properties);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        
        
        
        property_chooser.add(property);
        property_chooser.add(scrollPane);

        
        popup.add(new JLabel("<html>You have no more money, you must mortgage houses, hotels or property<br>You have $" + 
                broke.getMoney() + " Once your done mortgaging you can close the dialog"));
        popup.add(property_chooser);
        
        popup.add(houses);
        
        popup.add(mortgage_house);
        popup.add(mortgage_property);
        popup.add(retire);
        
        JDialog dialog = new JDialog();
        
        dialog.add(popup);
        dialog.setSize(new Dimension(600,400));
        dialog.setLocationRelativeTo(popup);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (broke.getMoney() < 0 && broke.getProperties().isEmpty())
                {
                    //player must retire
                    dialog.dispose();
                }
                else if (broke.getMoney() < 0) {
                   //dont close??
                }
                else {
                    dialog.dispose();
                }
                    
            }
            
        });
        

    }
    
}
