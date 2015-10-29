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
import monopoly.Player;

/* GridController
    - Maps each button to their grid object
    - Maps each grid to their property card
    - Helps player controller set players location on screen
    - Displays property cards when the grid's button is clicked
    
    The communication between the player controller and grid controller is kinda
    messy?? Can we improve this??
*/

public final class GridController implements ActionListener {
    
    final int PANELSIZE = 1020;
    Grid[] grid_object;
    HashMap<JButton,Grid> grid_buttons;
    HashMap<BuyableGrid, ImageIcon> property_cards;
    JButton[] grid_button;
    JPanel objects;
    
    
    public GridController(Grid[] g) {
        this.grid_object = g;
        this.grid_buttons = new HashMap();
        this.property_cards = new HashMap();
        setPropertyCards();
        assert(grid_button.length == grid_object.length);
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
        this.objects = objects;
    }
    
    /* This class needs the array of buttons from the board, maps them to the grid objects */
    public void addButtons(JButton[] buttons) {
        this.grid_button = buttons;
        for (int i = 0 ; i < buttons.length; i++) {
            grid_buttons.put(buttons[i],grid_object[i]);
        }
    }
    
    /* displays the property card when the grid button is clicked */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton pressed = (JButton) event.getSource();
        
        Grid grid = grid_buttons.get(pressed);
        
        if (grid instanceof BuyableGrid) {
            changeProperty((BuyableGrid) grid);
        }
    }
    
    /* Takes a buyable grid, retrieves its property card and displays it
    in the player menu using the player controller */
    public void changeProperty(BuyableGrid grid) {
        ImageIcon i = getPropertyCard(grid);
        String owner;
        if (grid.isBuyable()) {
            owner = "Unowned";
        }
        else {
            owner = "Owner: " + grid.getOwner().getName();
        }
        Game.player_controller.setProperty(i,owner);
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
    
    /* Given the grid's number, return a list on players on it */
    public LinkedList<Player> getOccupants(int i) {
        return grid_object[i].getOccupants();
    }
    
    /* Given the grid number, return a point where the player must be placed on the
    object layer, if there is one player on it
    */
    public Point setPlayerPosition(int i) {
        JButton grid = grid_button[i];
        Point point = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),objects);
        return point;
    }
    
    /* For when there are more than one player on the same grid.
    Given the grid number, and an array of the player's buttons
    set each players position on the screen.
    */
    public void setPlayersPositions(JButton[] buttons, int i) {
        JButton grid = grid_button[i];
        LinkedList<Player> occupants = getOccupants(i);
        int height = grid.getHeight();
        int width = grid.getWidth();
        Point grid_corner = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),objects);
        Point offset = new Point(grid.getX() + width/(occupants.size()+1), grid.getY() + height/(occupants.size())+1);
        offset = SwingUtilities.convertPoint(grid.getParent(), offset, objects);
        offset.setLocation(Math.abs(grid_corner.x - offset.x), Math.abs(grid_corner.y - offset.y));
        Point right_corner = SwingUtilities.convertPoint(grid.getParent(), grid.getX() + width, grid.getY() + height, objects);
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
