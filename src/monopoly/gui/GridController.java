/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import monopoly.Grid;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import monopoly.BuyableGrid;
import monopoly.Game;
import monopoly.AbstractPlayer;
import monopoly.PropertyGrid;

/* GridController
    - Maps each button to their grid object
    - Maps each grid to their property card
    - Helps player controller set players location on screen
    - Displays property cards when the grid's button is clicked
    - must add buttons and the object layer before use
    
    The communication between the player controller and grid controller is kinda
    messy?? Can we improve this??
*/

public final class GridController implements ActionListener {
    
    final int PANELSIZE = 1020;
    Grid[] grid_object;
    HashMap<JButton, Grid> button_to_grid;
    HashMap<Grid,JButton> grid_to_button;
    HashMap<Grid, LinkedList<JButton>> grid_houses;
    HashMap<BuyableGrid, ImageIcon> property_cards;
    JButton[] grid_button;
    JPanel object_layer;
    ImageIcon house_icon,hotel_icon;
    
    
    public GridController(Grid[] g) {
        this.grid_object = g;
        this.grid_to_button = new HashMap();
        this.button_to_grid = new HashMap();
        this.property_cards = new HashMap();
        this.grid_houses = new HashMap();
        setPropertyCards();
        this.house_icon = new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/house.png"));
        for (Grid grid : g) {
            grid_houses.put(grid, new LinkedList<>());
        }

    }
    
    /* Get a grid object from its number */
    public Grid getGrid(int i) {
        return grid_object[i];
    }
    
    /* Get a property card Image Icon from its corresponsing BuyableGrid object */
    public ImageIcon getPropertyCard(BuyableGrid g) {
        return property_cards.get(g);
    }
    
    /* This class needs the layer where objects lay, in order to move players */
    public void addObjectLayer(JPanel objects) {
        this.object_layer = objects;
    }
    
    /* This class needs the array of buttons from the board, maps them to the grid objects */
    public void addButtons(JButton[] buttons) {
        this.grid_button = buttons;
        for (int i = 0 ; i < buttons.length; i++) {
            button_to_grid.put(buttons[i],grid_object[i]);
            grid_to_button.put(grid_object[i],buttons[i]);
        }
    }
    
    /* displays the property card when the grid button is clicked */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton pressed = (JButton) event.getSource();
        
        Grid grid = button_to_grid.get(pressed);
        
        if (grid instanceof BuyableGrid) {
            changeProperty((BuyableGrid) grid);
        }
    }
    
    /* Takes a buyable grid, retrieves its property card and displays it
    in the player menu using the player controller */
    public void changeProperty(BuyableGrid grid) {
        Game.player_controller.clearHouseIcons();
        Game.player_controller.clearBuyButton();
        ImageIcon i = getPropertyCard(grid);
        String owner;
        if (grid.isBuyable()) {
            owner = "Unowned";
        }
        else {
            owner = "Owner: " + grid.getOwner().getName();
            
        }
        if (grid instanceof PropertyGrid) {
            
            PropertyGrid property = (PropertyGrid) grid;
            if (property.getOwner() == Game.current_player) {
                displayBuyHouseButton(property);
            }
            if (property.getCurrentHouses() > 0) {
                Game.player_controller.setHouseIcons(property.getCurrentHouses());
            }
            
        }

        Game.player_controller.setProperty(i,owner);
    }
    
    /* Display the buy house button if the owner has all the properties in the monopoly */
    public void displayBuyHouseButton(PropertyGrid grid) {
//TODO(nick): Implement logic that only displays the button when necessary
        if (Game.current_player.ownsAllType(grid)) 
            Game.player_controller.setBuyHouse(grid);
    }
    
    /* Creates ImageIcons for the property cards and create a mapping from Property to property card */
    public void setPropertyCards(){
        for (Grid grid_object1 : grid_object) {
            if (grid_object1 instanceof BuyableGrid) { //is there a better way to do this?
                ImageIcon image = new ImageIcon(getClass().getResource("/monopoly/gui/img/propertycards/" + grid_object1.getName() + ".jpg"));
                property_cards.put((BuyableGrid) grid_object1, image);
            }
        }
    }
    
    /* Given the grid's number, return a list of players on it */
    public LinkedList<AbstractPlayer> getOccupants(int i) {
        return grid_object[i].getOccupants();
    }
    
    /*This is used to add a house icon on the corresponding grid on the GUI,
    
    */
    public void addHouseIcon(PropertyGrid grid) {
        
        JButton button = grid_to_button.get(grid);
        boolean vertical = button.getWidth() < button.getHeight();
        Point corner = SwingUtilities.convertPoint(button.getParent(), button.getLocation(), object_layer);
        double x = 0;
        double y = 0;
        switch (button.getParent().getName()) {
            case "north":
                corner.setLocation(corner.getX(), corner.getY() + button.getHeight() - house_icon.getIconHeight());
                x = corner.getX() - 7;
                y = corner.getY();
                break;
            case "west":
                corner.setLocation(corner.getX() + button.getWidth() - house_icon.getIconWidth(), corner.getY());
                x = corner.getX();
                y = corner.getY() - 7;
                break;
            case "south":
                x = corner.getX() - 7;
                y = corner.getY();
                break;
            case "east":
                x = corner.getX();
                y = corner.getY() - 7;
                break;
        }
        
        LinkedList<JButton> house_buttons = grid_houses.get(grid);
        
        if (house_buttons.size() == 0) {
        }
        else {
            JButton last_house = house_buttons.getLast();
            x = last_house.getX();
            y = last_house.getY();
            int width = house_icon.getIconWidth();
            int height = house_icon.getIconHeight();
            if (vertical) {
                x += width - 7; //allow a overlap of 7 pixels
            }
            else {
                y += height - 3; //allow overlap of 3 pixels, when horizontal house icon have more room
            }
        }
        JButton house = new JButton();
        house.setIcon(house_icon);
        house.setLocation((int)x,(int)y);
        house.setBounds((int)x,(int)y,house_icon.getIconWidth(),house_icon.getIconHeight());
        house.setBorderPainted(false);
        house.setContentAreaFilled(false);
        house.setFocusPainted(false); 
        house.setOpaque(false);
        object_layer.add(house);
        house_buttons.add(house);  
        house.setVisible(true);
        object_layer.repaint();
    }
    
    /* Given the grid number, return a point where the player must be placed on the
    object layer, if there is one player on it
    */
    public Point setPlayerPosition(int i) {
        JButton grid = grid_button[i];
        Point point = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),object_layer);
        return point;
    }
    
    /* For when there are more than one player on the same grid.
    Given the grid number, and an array of the player's buttons
    set each players position on the screen.
    THIS NEEDS IMPROVEMENT BUT WORKS FOR NOW
    */
    public void setPlayersPositions(JButton[] buttons, int i) {
        JButton grid = grid_button[i];
        LinkedList<AbstractPlayer> occupants = getOccupants(i);
        int height = grid.getHeight();
        int width = grid.getWidth();
        Point grid_corner = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),object_layer);
        Point offset = new Point(grid.getX() + width/(occupants.size()+1), grid.getY() + height/(occupants.size())+1);
        offset = SwingUtilities.convertPoint(grid.getParent(), offset, object_layer);
        offset.setLocation(Math.abs(grid_corner.x - offset.x), Math.abs(grid_corner.y - offset.y));
        Point right_corner = SwingUtilities.convertPoint(grid.getParent(), grid.getX() + width, grid.getY() + height, object_layer);
        int x = grid_corner.x;
        int y = grid_corner.y;
        if (occupants.size() > 3) { //if there are more than 3 on a grid move first player a little bit upwards.
            x -= offset.x/2;
            y -= offset.y/2;
            grid_corner.setLocation(x,y);
        }
        for (JButton b : buttons) {
            b.setLocation(grid_corner);
            if (x + offset.x < right_corner.x - b.getWidth()/1.3) { // lets token only side off the grid by a bit
                x += offset.x;
            }
            if (y + offset.y < right_corner.y - b.getHeight()/1.3) {
                y += offset.y;
            }
            grid_corner.setLocation(x,y);
        }
    }
    
}
