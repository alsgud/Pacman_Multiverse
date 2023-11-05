// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends GameGrid
{
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);
  private boolean isMultiverse;
  protected PacActor pacActor = new PacActor(this);
  private Monster troll = new Troll(this, MonsterType.Troll);
  private Monster orion = new Orion(this, MonsterType.Orion);
  private Monster tx5 = new TX5(this, MonsterType.TX5);
  private Monster wizard = new Wizard(this, MonsterType.Wizard);
  private Monster alien = new Alien(this, MonsterType.Alien);
  private Item items = new Item(this);
  private Ice ice = new Ice(this);
  private Gold gold = new Gold(this);
  public boolean monstersFrozen = false;
  public boolean monstersAngry = false;
  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;


  public Game(GameCallback gameCallback, Properties properties)
  {
    //Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    this.gameCallback = gameCallback;
    this.properties = properties;
    setSimulationPeriod(100);
    setTitle("[PacMan in the Multiverse]");

    //Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    loadPillAndItemsLocations();

    GGBackground bg = getBg();
    drawGrid(bg);

    //Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    orion.setSeed(seed);
    wizard.setSeed(seed);
    troll.setSeed(seed);
    tx5.setSeed(seed);
    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(150);
    orion.setSlowDown(3);
    wizard.setSlowDown(3);
    troll.setSlowDown(3);
    tx5.setSlowDown(3);
    alien.setSlowDown(3);
    pacActor.setSlowDown(3);
    tx5.stopMoving(5);
    setupGameVersion();
    setupActorLocations();

    //Run the game
    doRun();
    show();
    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit;
    boolean hasPacmanEatAllPills;
    items.setupPillAndItemsLocations();
    int maxPillsAndItems = items.countPillsAndItems();
    
    do {
      if (isMultiverse){
        hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
              tx5.getLocation().equals(pacActor.getLocation()) || orion.getLocation().equals(pacActor.getLocation())
              || wizard.getLocation().equals(pacActor.getLocation()) ||
              alien.getLocation().equals(pacActor.getLocation());
      }
      else {
        hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
              tx5.getLocation().equals(pacActor.getLocation());
      }
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    Location loc = pacActor.getLocation();
    troll.setStopMoving(true);
    tx5.setStopMoving(true);
    orion.setStopMoving(true);
    wizard.setStopMoving(true);
    alien.setStopMoving(true);
    pacActor.removeSelf();

    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else if (hasPacmanEatAllPills) {
      bg.setPaintColor(Color.yellow);
      title = "YOU WIN";
    }
    setTitle(title);
    gameCallback.endOfGame(title);

    doPause();
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  //method obtains version information from properties file and sets corresponding boolean
  private void setupGameVersion(){
    String gameVersion = this.properties.getProperty("version");
    if (gameVersion.equals("simple")){
      isMultiverse = false;
    }
    else {
      isMultiverse = true;
    }
  }
  //method reads in properties file and initialises monsters with their location information
  private void setupActorLocations() {
    String[] trollLocations = this.properties.getProperty("Troll.location").split(",");
    String[] tx5Locations = this.properties.getProperty("TX5.location").split(",");
    String[] pacManLocations = this.properties.getProperty("PacMan.location").split(",");

    int trollX = Integer.parseInt(trollLocations[0]);
    int trollY = Integer.parseInt(trollLocations[1]);

    int tx5X = Integer.parseInt(tx5Locations[0]);
    int tx5Y = Integer.parseInt(tx5Locations[1]);

    int pacManX = Integer.parseInt(pacManLocations[0]);
    int pacManY = Integer.parseInt(pacManLocations[1]);

    addActor(troll, new Location(trollX, trollY), Location.NORTH);
    addActor(pacActor, new Location(pacManX, pacManY));
    addActor(tx5, new Location(tx5X, tx5Y), Location.NORTH);

    if (isMultiverse) {
      String[] orionLocations = this.properties.getProperty("Orion.location").split(",");
      String[] wizardLocations = this.properties.getProperty("Wizard.location").split(",");
      String[] alienLocations = this.properties.getProperty("Alien.location").split(",");

      int orionX = Integer.parseInt(orionLocations[0]);
      int orionY = Integer.parseInt(orionLocations[1]);

      int wizardX = Integer.parseInt(wizardLocations[0]);
      int wizardY = Integer.parseInt(wizardLocations[1]);

      int alienX = Integer.parseInt(alienLocations[0]);
      int alienY = Integer.parseInt(alienLocations[1]);

      addActor(orion, new Location(orionX, orionY), Location.NORTH);
      addActor(wizard, new Location(wizardX, wizardY), Location.NORTH);
      addActor(alien, new Location(alienX, alienY), Location.NORTH);
    }
  }

  public ArrayList<Location> getPillAndItemLocations() {
    return items.getPillAndItemLocations();
  }

  //stops monsters movement for 3 seconds after PacActor eats ice cube
  public void freezeMonsters() {
    if (isMultiverse) {
      monstersFrozen = true;
      Timer timer = new Timer(); // Instantiate Timer Object
      int SECOND_TO_MILLISECONDS = 1000;
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          monstersFrozen = false;
        }
      }, 3 * SECOND_TO_MILLISECONDS);

      troll.stopMoving(3);
      tx5.stopMoving(3);
      alien.stopMoving(3);
      wizard.stopMoving(3);
      orion.stopMoving(3);
    }
  }
  //makes monsters move 2 cells when gold piece is eaten by pacActor if they are not frozen
  public void angerMonsters(){
    if (!monstersFrozen && isMultiverse) {
      monstersAngry = true;
      Timer timer = new Timer(); // Instantiate Timer Object
      int SECOND_TO_MILLISECONDS = 1000;
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          monstersAngry = false;
        }
      }, 3 * SECOND_TO_MILLISECONDS);
    }
  }
  /*
  function reads in properties file and calls functions from item class to add Pill and Gold locations into arraylist
  stored in item class
  */
  private void loadPillAndItemsLocations() {
    String pillsLocationString = properties.getProperty("Pills.location");
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        items.addPillLocation(locationStrings);
      }
    }

    String goldLocationString = properties.getProperty("Gold.location");
    if (goldLocationString != null) {
      String[] singleGoldLocationStrings = goldLocationString.split(";");
      for (String singleGoldLocationString: singleGoldLocationStrings) {
        String[] locationStrings = singleGoldLocationString.split(",");
        items.addGoldLocation(locationStrings);
      }
    }
  }

  //function retrieves location information from item arraylists and places item on game map
  private void drawGrid(GGBackground bg)
  {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && items.getPropertyPillLocations().size() == 0) { // Pill
          items.putItem(bg, location);
        } else if (a == 3 && items.getPropertyGoldLocations().size() == 0) { // Gold
          gold.putItem(bg, location, items);
        } else if (a == 4) {
          ice.putItem(bg, location, items);
        }
      }
    }

    for (Location location : items.getPropertyPillLocations()) {
      items.putItem(bg, location);
    }

    for (Location location : items.getPropertyGoldLocations()) {
      gold.putItem(bg, location, items);
    }
  }

  /*
  function used by PacActor class when it calls eatPill()
  removes item from game map after pacActor lands on the same cell as the respective items
  */
  public void removeItem(String type,Location location){
    if(type.equals("gold")){
      for (Actor item : items.getGoldPieces()){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }else if(type.equals("ice")){
      for (Actor item : items.getIceCubes()){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }
  }

  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }
}
