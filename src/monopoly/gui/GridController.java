/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import monopoly.AbstractPlayer;
import monopoly.BuyableGrid;
import monopoly.Game;
import monopoly.Grid;

/**
 *    Grid Controller, maps the grid objects to the button on the GUI.<br>
 * <br>
 *   - Maps each button to their grid object <br>
 *   - Maps each grid to their property card <br>
 *   - If a grid is clicked, it is shown on the player menu <br>
 * 
 */
public class GridController implements ActionListener {
    
    Grid[] grid_objects;
    JButton[] grid_buttons;
    HashMap<JButton, Grid> button_to_grid;
    HashMap<Grid,JButton> grid_to_button;
    HashMap<BuyableGrid, ImageIcon> property_cards; 
    HashMap<Grid, LinkedList<JButton>> grid_houses;
    
    public GridController(Grid[] g)
    {
       this.grid_objects = g;
       this.grid_to_button = new HashMap();
       this.button_to_grid = new HashMap();
       this.property_cards = new HashMap();
       this.grid_houses = new HashMap();
       setPropertyCards();
       for (Grid grid : g) {
            grid_houses.put(grid, new LinkedList<>());
       }
    }
    
    /**Maps buttons to grids and grids to buttons
     */
    public void addButtons(JButton[] buttons) {
        this.grid_buttons = buttons;
        for (int i = 0 ; i < grid_buttons.length; i++) {
            button_to_grid.put(grid_buttons[i],grid_objects[i]);
            grid_to_button.put(grid_objects[i],grid_buttons[i]);
        }
    }
    
        
    /**
     * Creates ImageIcons for the property cards and create a mapping from Property to property card
     */
    private void setPropertyCards(){
        for (Grid grid_object1 : grid_objects) {
            if (grid_object1 instanceof BuyableGrid) { //is there a better way to do this?
                ImageIcon image = new ImageIcon(getClass().getResource("/monopoly/gui/img/propertycards/" + grid_object1.getName() + ".jpg"));
                property_cards.put((BuyableGrid) grid_object1, image);
            }
        }
    }
    
     /**
     * Get a grid object by it's number
     * @param i the number of grid to get
     * @return the grid with the corresponding number
     */
    public Grid getGrid(int i) {
        return grid_objects[i];
    }
    
    public Grid getGrid(JButton button)
    {
        return button_to_grid.get(button);
    }

    public JButton getButton(int i)
    {
        return grid_buttons[i];
    }
    
    public JButton getButton(Grid g)
    {
        return grid_to_button.get(g);
    }

    public LinkedList<JButton> getHouses(Grid g)
    {
        return grid_houses.get(g);
    }
    /**
     * Get a property card or title deed Image Icon from its corresponding BuyableGrid object
     * @param g the buyable grid
     * @return the buyable grid's title deed
     */

    public ImageIcon getPropertyCard(BuyableGrid g) {
        return property_cards.get(g);
    }
    
    
    /**
     * Given the grid's number, return a list of players on it
     * @param i the number of the grid
     * @return players currently on the grid
     */

    public LinkedList<AbstractPlayer> getOccupants(int i) {
        return Game.board_grids[i].getOccupants();
    }
    
    /**
     * Displays the property card when the grid button is clicked
     * @param event the event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton pressed = (JButton) event.getSource();
        
        Grid grid = button_to_grid.get(pressed);
        
        if (grid instanceof BuyableGrid) {
            Game.menu_controller.setProperty((BuyableGrid) grid);
        }
    }

    
}
