// Monster class which is a parent class to all types of monsters
// Used for PacMan
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Monster extends Actor
{
  protected Game game;
  public MonsterType type;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private final int listLength = 10;
  public boolean stopMoving = false;
  private int seed = 0;
  public Random randomiser = new Random(0);

  public Monster(Game game, MonsterType type)
  {
    super("sprites/" + type.getImageName());
    this.game = game;
    this.type = type;
  }

  public void stopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }
  // if state is angry, take extra step
  private void walkApproach(){}
  //function contains logic for when monster movement after collecting gold piece
  public void angryApproach(Double direction){
    if (game.monstersAngry){
      setDirection(direction);
      Location next = getNextMoveLocation();
      if (canMove(next)) {
        setLocation(next);
      }
    }
  }
  // use Tx5's old algorithm to move to location
  public void walkTowards(Location location) {
    double oldDirection = getDirection();
    Location.CompassDirection compassDir =
            getLocation().get4CompassDirectionTo(location);
    Location next = getLocation().getNeighbourLocation(compassDir);
    setDirection(compassDir);
    if (!isVisited(next) && canMove(next))
    {
      setLocation(next);
    }
    else
    {
      // Random walk
      int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
      setDirection(oldDirection);
      turn(sign * 90);  // Try to turn left/right
      next = getNextMoveLocation();
      if (canMove(next))
      {
        setLocation(next);
      }
      else
      {
        setDirection(oldDirection);
        next = getNextMoveLocation();
        if (canMove(next)) // Try to move forward
        {
          setLocation(next);
        }
        else
        {
          setDirection(oldDirection);
          turn(-sign * 90);  // Try to turn right/left
          next = getNextMoveLocation();
          if (canMove(next))
          {
            setLocation(next);
          }
          else
          {

            setDirection(oldDirection);
            turn(180);  // Turn backward
            next = getNextMoveLocation();
            setLocation(next);
          }
        }
      }
    }
    game.getGameCallback().monsterLocationChanged(this);
    addVisitedList(next);
  }
  public void act()
  {
    if (stopMoving) {
      return;
    }
    walkApproach();
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }
  // calculation of distance between two locations
  public int findDistance(Location start, Location dest){
    int distance, distanceX, distanceY;
    distanceX = Math.abs(dest.getX() - start.getX());
    distanceY = Math.abs(dest.getY() - start.getY());
    distance = distanceX + distanceY;
    return distance;
}

  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }

  public void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  public boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  public boolean canMove(Location location)
  {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
          || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }

  public MonsterType getType() {
    return this.type;
  }
}
