/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import monopoly.BuyableGrid;
import monopoly.Game;
import monopoly.AbstractPlayer;
import monopoly.PropertyGrid;


/**
 * PlayerController, manages the components on the player menu. <br>
 * <br>
 * - Initializes and updates player menu <br>
 * - Sets the property card in the menu and acts as a listener for the list of
 *   properties on the menu. <br>
 * 
 *
 */
public final class PlayerMenuController implements ListSelectionListener {

    
    private final HashMap<String, Component> menu_components;
    
    private JPanel player_menu;


    // The current PropertyGrid that is being displayed (if no property displayed then contains the last property displayed)

    /**
     * The last property grid selected on the menu
     */
    public PropertyGrid selectedProperty;

    /**
     * The grid selected on the menu
     */
    public BuyableGrid selectedGrid;
    
    /**
     * Creates a new player controller
     *
     */
    public PlayerMenuController() {
        
        menu_components = new HashMap<>();
    }

    /**
     * Updates the menu to show another players details
     *
     * @param p the player to display on the menu
     */
    public void updateMenu(AbstractPlayer p) {
        JLabel name = (JLabel) menu_components.get("Name");

        name.setText(p.getName());
        name.setIcon(Game.object_controller.getPlayerIcon(p));

        updateStats(p);

        JList properties = (JList) menu_components.get("Property");
        properties.clearSelection();

        DefaultListModel list = (DefaultListModel) properties.getModel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                list.removeAllElements();
                List<BuyableGrid> properties = p.getProperties();
                //sorts properties by alphabetically by type
                properties.sort(new Comparator<BuyableGrid>() {
                    @Override
                    public int compare(BuyableGrid g1, BuyableGrid g2) {
                        return g1.property_group.toString().compareTo(g2.property_group.toString());
                    }
                });
                for (BuyableGrid g : properties) {
                    list.addElement(g);
                }
            }
        });

        clearPropertyInfo();
        clearBuyButton();
        clearHouseIcons();
        clearTradeButton();
    }

    /**
     * Updates the money, jail free cards, displayed for the player on the
     * player menu
     *
     * @param p the player to update the stats
     */
    public void updateStats(AbstractPlayer p) {
        JLabel money = (JLabel) menu_components.get("Money");
        money.setText("<html>$" + p.getMoney() + "<br>Get out of Jail Free cards: " + p.getNumberOfJailFreeCards() + "</html>");
    }

    /* Initializes all the menu compenents, and adds them to a hashtable, where
    their key's are strings such as
    "Name", "Money", "Property", "PropertyInfo"
     */
    /**
     * Initializes the player menu
     *
     * @param player_menu the panel where the player menu is
     */
    public void initMenu(JPanel player_menu) {
        this.player_menu = player_menu;

        JLabel PlayerName = new JLabel();
        PlayerName.setHorizontalAlignment(SwingConstants.LEFT);
        PlayerName.setFont(new java.awt.Font("Ubuntu", 1, 30)); // NOI18N
        PlayerName.setText("PlayerName");

        player_menu.add(PlayerName);

        JLabel Money = new JLabel();
        Money.setFont(new java.awt.Font("Ubuntu", 1, 16));
        Money.setText("Players Money");
        Money.setSize(200, Money.getHeight());

        player_menu.add(Money);

        DefaultListModel property_list = new DefaultListModel();
        JList properties = new JList(property_list);
        properties.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(properties);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        player_menu.add(scrollPane);

        JButton saveGameButton = new JButton();
        saveGameButton.setBounds(920, 920, 100, 40);
        saveGameButton.setPreferredSize(new java.awt.Dimension(100, 40));
        saveGameButton.setContentAreaFilled(false);
        saveGameButton.setFocusPainted(false);
        saveGameButton.setOpaque(false);
        saveGameButton.setFont(new java.awt.Font("Ubuntu", 1, 15));
        saveGameButton.setMargin(new Insets(3, 3, 3, 3));
        saveGameButton.setText("Save Game");
        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.saveGame();
                JOptionPane.showMessageDialog(null, "The game has been saved.");
            }
        });

        player_menu.add(saveGameButton);

        JLabel property_info = new JLabel();
        property_info.setHorizontalTextPosition(JLabel.CENTER);
        property_info.setVerticalTextPosition(JLabel.TOP);

        player_menu.add(property_info);

        JPanel house_icons = new JPanel(new FlowLayout());
        house_icons.setOpaque(false);
        house_icons.setVisible(false);

        player_menu.add(house_icons);

        JButton buyHouseButton = new JButton();
        buyHouseButton.setBounds(920, 920, 100, 40);
        buyHouseButton.setPreferredSize(new java.awt.Dimension(100, 40));
        buyHouseButton.setContentAreaFilled(false);
        buyHouseButton.setFocusPainted(false);
        buyHouseButton.setOpaque(false);
        buyHouseButton.setFont(new java.awt.Font("Ubuntu", 1, 15));
        buyHouseButton.setMargin(new Insets(3, 3, 3, 3));
        buyHouseButton.setText("Buy House");
        buyHouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.dialog_controller.buyHouse();
            }
        });

        player_menu.add(buyHouseButton);
        
        JButton trade = new JButton();
        trade.setBounds(920, 920, 100, 40);
        trade.setPreferredSize(new java.awt.Dimension(100, 40));
        trade.setContentAreaFilled(false);
        trade.setFocusPainted(false);
        trade.setOpaque(false);
        trade.setFont(new java.awt.Font("Ubuntu", 1, 15));
        trade.setMargin(new Insets(3, 3, 3, 3));
        trade.setText("Trade");
        trade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.dialog_controller.createTradeRequest(Game.current_player, selectedGrid.getOwner(), selectedGrid);
            }
        });

        player_menu.add(trade);

        menu_components.put("Money", Money);
        menu_components.put("Name", PlayerName);
        menu_components.put("Property", properties);
        menu_components.put("PropertyInfo", property_info);
        menu_components.put("BuyHouse", buyHouseButton);
        menu_components.put("HouseIcons", house_icons);
        menu_components.put("Trade", trade);
    }

    /**
     * Displays the buy house button
     *
     * @param g the corresponding grid to show the buy house button under
     */
    public void setBuyHouse(PropertyGrid g) {
        JButton button = (JButton) menu_components.get("BuyHouse");
        button.setVisible(true);
        selectedProperty = g;
        if (g.getCurrentHouses() == g.MAX_NUMBER_HOUSES)
            button.setText("Buy Hotel");
        else
            button.setText("Buy House");
    }
    
    /**
     * Shows the button to open a trade dialog
     */
    public void showTradeButton()
    {
        menu_components.get("Trade").setVisible(true);
    }


    /**
     * Displays a title deed and the owner on the player menu
     * @param grid
     */
    public void setProperty(BuyableGrid grid) {
        
        clearHouseIcons();
        clearBuyButton();
        clearTradeButton();
        
        ImageIcon i = Game.grid_controller.getPropertyCard(grid);
        
        String owner;
        
        if (grid.isBuyable()) {
            owner = "Unowned";
        }
        else {
            owner = "Owner: " + grid.getOwner().getName();
            
        }
        
        Game.menu_controller.selectedGrid = grid;
        
        if (grid.getOwner() != null && grid.getOwner() != Game.current_player)
                menu_components.get("Trade").setVisible(true);
        
        if (grid instanceof PropertyGrid) {
            
            PropertyGrid property = (PropertyGrid) grid;
            if (property.getOwner() == Game.current_player && 
                    Game.current_player.ownsAllType(property) && 
                    property.getCurrentHotels() != property.MAX_NUMBER_HOTELS) {
                setBuyHouse(property);
            }
            if (property.getCurrentHouses() > 0 || property.getCurrentHotels() > 0) {
                Game.menu_controller.setHouseorHotelIcons(property);
            }
            
        }
        
        JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
        property_info.setIcon(i);
        property_info.setText(owner);
    }
    

    /**
     * Sets the number of houses to display under the property card on the player menu
     * @param houses
     */
    public void setHouseorHotelIcons(PropertyGrid property) {
        int houses = property.getCurrentHouses();
        int hotels = property.getCurrentHotels();
        
        JPanel panel = (JPanel) menu_components.get("HouseIcons");
        panel.setVisible(true);
        for (int i = 0; i < houses; i++) {
            JButton house = new JButton();
            ImageIcon house_icon = Game.object_controller.house_icon;
            house.setIcon(house_icon);
            house.setBorderPainted(false);
            house.setContentAreaFilled(false);
            house.setFocusPainted(false);
            house.setOpaque(false);
            house.setVisible(true);
            panel.add(house);
        }
        for (int i = 0; i < hotels; i++)
        {
            JButton hotel = new JButton();
            ImageIcon hotel_icon = Game.object_controller.hotel_icon;
            hotel.setIcon(hotel_icon);
            hotel.setBorderPainted(false);
            hotel.setContentAreaFilled(false);
            hotel.setFocusPainted(false);
            hotel.setOpaque(false);
            hotel.setVisible(true);
            panel.add(hotel);
        }
        panel.repaint();
        panel.revalidate();
    }

    /**
     * Changes the property shown on the player menu to be the property selected on
     * the list of properties owned on the player menu
     * @param e the event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        if (list.getSelectedIndex() == -1) { //nothing is selected, clear menu
            clearPropertyInfo();
            clearBuyButton();
            clearHouseIcons();
        } else {
            setProperty((BuyableGrid) list.getSelectedValue());
        }
    }
 
    /**
     * Clears the property info on the menu
     */
    public void clearPropertyInfo() {
        JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
        property_info.setText("");
        property_info.setIcon(null);
    }

    /**
     * Sets the buy houses/hotels button to not be visible
     */
    public void clearBuyButton() {
        JButton button = (JButton) menu_components.get("BuyHouse");
        button.setVisible(false);
    }
    
    /**
     * Clears the trade button
     */
    public void clearTradeButton() {
        JButton button = (JButton) menu_components.get("Trade");
        button.setVisible(false);
    }

    /**
     * Clears the house icons on the player menu
     */
    public void clearHouseIcons() {
        JPanel houses = (JPanel) menu_components.get("HouseIcons");
        houses.removeAll();
    }
}
