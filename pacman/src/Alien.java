package src;

import ch.aplu.jgamegrid.Location;

public class Alien extends Monster{

    public Alien(Game game, MonsterType type) {
        super(game, type);
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

    private void walkApproach()
    {
        // find all possible movable locations
        Location[] locations = new Location[8];
        int locationIter = 0;
        for (int direction = 0; direction < 8; direction++){
            turn(45 * direction);
            Location next = getNextMoveLocation();
            if (canMove(next)){
                locations[locationIter] = next;
                locationIter++;
            }
        }

        int shortestDistanceFound = 99;
        Location shortestLocation = null;
        // for possible moves, calculate distance from pacman
        for (int i = 0; i < locationIter; i ++){
            int distance = findDistance(locations[i], game.pacActor.getLocation());
            if (distance < shortestDistanceFound){
                shortestDistanceFound = distance;
                shortestLocation = locations[i];
            }
            if (distance == shortestDistanceFound && randomiser.nextBoolean()){
                shortestDistanceFound = distance;
                shortestLocation = locations[i];
            }
        }
        setLocation(shortestLocation);
        angryApproach(getDirection());
    }
    public boolean canMove(Location location)
    {
        return super.canMove(location);
    }
    public void setStopMoving(boolean stopMoving) {
        super.setStopMoving(stopMoving);
    }
}

