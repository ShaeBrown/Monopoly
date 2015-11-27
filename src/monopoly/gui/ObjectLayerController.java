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
import java.util.List;
import javax.swing.Icon;

import javax.swing.ImageIcon;


import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import monopoly.Game;
import monopoly.AbstractPlayer;
import monopoly.PropertyGrid;

/**
 * GridController, controls the GUI interaction for all the objects on the Object Layer. <br>
 *   <br>
 *   - Set players location on screen <br>
 *   - Arranges the house tokens on the grid <br>
 *   
 * 
 */


public final class ObjectLayerController {
    
    final int PANELSIZE = 1020;

    JPanel object_layer;
    ImageIcon house_icon,hotel_icon;
    List<AbstractPlayer> players;
    private final HashMap<AbstractPlayer, JButton> player_to_button;
    private final HashMap<JButton, AbstractPlayer> button_to_player;
    
    /**
     * Creates a new grid controller
     * @param players the list of players in the game
     */
    public ObjectLayerController(List<AbstractPlayer> players) {     
        this.players = players;
        player_to_button = new HashMap<>();
        button_to_player = new HashMap<>();
    }
    
    /**
     * Initializes all the players onto the board
     */
    public void initPlayers() {
        for (AbstractPlayer p : players) {
            JButton button = new JButton();
            button.setIcon(createPlayerToken(p));
            button.setBounds(0, 0, 75, 75);
            button.setPreferredSize(new java.awt.Dimension(75, 75));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            object_layer.add(button);
            player_to_button.put(p, button);
            button_to_player.put(button, p);
        }
        updatePlayersPositions(new LinkedList(players));
    }
    
    /**
     * Returns the appropriate ImageIcon for the players token
     * @param p the player
     * @return their token
     */
    private ImageIcon createPlayerToken(AbstractPlayer p) {
        return new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/" + p.getToken() + ".png"));
    }
    
    /**
     *
     * @param p
     * @return
     */
    public Icon getPlayerIcon(AbstractPlayer p)
    {
        return player_to_button.get(p).getIcon();
    }



    /**
     * This class needs the layer where objects lay, in order to move players and add houses
     * @param objects the layer where objects lay on the GUI
     */

    public void addObjectLayer(JPanel objects) {
        this.object_layer = objects;
    }
    
    

    /**
     * Add a house icon on the corresponding grid on the GUI,
     * @param grid the grid to add a house
     */

    public void addHouseIcon(PropertyGrid grid) {
        
        JButton button = Game.grid_controller.grid_to_button.get(grid);
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
        
        LinkedList<JButton> house_buttons = Game.grid_controller.getHouses(grid);
        
        if (house_buttons.isEmpty()) {
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
    


    /**
     * Given the grid number, return a point where the player must be placed on the
     * object layer, if there is a single player on it
     * @param p
     */
    public void updatePlayerPosition(AbstractPlayer p) {
        JButton button = player_to_button.get(p);
        int location = p.getLocation();
        LinkedList<AbstractPlayer> occupants = Game.grid_controller.getOccupants(location);
        if (occupants.size() == 1) {
            JButton grid = Game.grid_controller.getButton(location);
            Point point = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),object_layer);
            button.setLocation(point);
        } 
        else {
            updatePlayersPositions(occupants);
        }
    }

    
    //NEEDS IMPROVEMENT
    /**
     * Arranges players on a grid when there is more than one player occupying the grid.
     * Given the grid number, and an array of the player's buttons,
     * set each players position on the screen.
     * @param buttons the buttons to arrange
     * @param i the number of the grid
     */
    private void updatePlayersPositions(LinkedList<AbstractPlayer> players) {
        JButton[] buttons = new JButton[players.size()];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = player_to_button.get(players.get(i));
        }
        int location = players.get(0).getLocation();
        JButton grid = Game.grid_controller.getButton(location);
        LinkedList<AbstractPlayer> occupants = Game.grid_controller.getOccupants(location);
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
