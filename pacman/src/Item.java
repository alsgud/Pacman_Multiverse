//class deals with logic relating to items in game. Parent class of all item types.
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

class Item extends Actor {
    protected Game game;
    protected ArrayList<Actor> iceCubes = new ArrayList<Actor>();
    protected ArrayList<Actor> goldPieces = new ArrayList<Actor>();
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();
    private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();

    Item(Game game){
        this.game = game;
    }

    //function places a pill on the game map.
    public void putItem(GGBackground bg, Location location) {
        bg.fillCircle(game.toPoint(location), 5);
    }
    /*
    function counts number of pill and gold present on map. Uses grid initialised on PacManGameGrid if no information
    specified on property
    else count pills and gold via items loaded into array list of pills and gold locations by the property file
    */
    public int countPillsAndItems() {
        int pillsAndItemsCount = 0;
        for (int y = 0; y < game.getNumVertCells(); y++)
        {
            for (int x = 0; x < game.getNumHorzCells(); x++)
            {
                Location location = new Location(x, y);
                int a = game.grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    pillsAndItemsCount++;
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    pillsAndItemsCount++;
                }
            }
        }
        if (propertyPillLocations.size() != 0) {
            pillsAndItemsCount += propertyPillLocations.size();
        }

        if (propertyGoldLocations.size() != 0) {
            pillsAndItemsCount += propertyGoldLocations.size();
        }

        return pillsAndItemsCount;
    }
    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }
    public void addPillLocation(String[] locationStrings) {
        propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
    }
    public ArrayList<Location> getPropertyPillLocations() {
        return propertyPillLocations;
    }
    //function used to add gold location to array list in game class
    public void addGoldLocation(String[] locationStrings) {
        propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
    }
    public ArrayList<Location> getPropertyGoldLocations(){
        return propertyGoldLocations;
    }
    /*
    function used to load in pill and gold locations into array list from property if information exists,
    else uses map set up on PacManGameGrid
    * */
    public void setupPillAndItemsLocations() {
        for (int y = 0; y < game.getNumVertCells(); y++)
        {
            for (int x = 0; x < game.getNumHorzCells(); x++)
            {
                Location location = new Location(x, y);
                int a = game.grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 3 &&  propertyGoldLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 4) {
                    pillAndItemLocations.add(location);
                }
            }
        }

        if (propertyPillLocations.size() > 0) {
            for (Location location : propertyPillLocations) {
                pillAndItemLocations.add(location);
            }
        }
        if (propertyGoldLocations.size() > 0) {
            for (Location location : propertyGoldLocations) {
                pillAndItemLocations.add(location);
            }
        }
    }

    public ArrayList<Actor> getGoldPieces(){
        return goldPieces;
    }
    public ArrayList<Actor> getIceCubes(){
        return iceCubes;
    }
}