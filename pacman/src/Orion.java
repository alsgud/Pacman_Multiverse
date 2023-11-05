package src;

import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orion extends Monster{
    List<Location> pillAndItemLocations;
    private ArrayList<Location> goldLocations = new ArrayList<Location>();
    public Orion(Game game, MonsterType type) {
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
    public void walkApproach()
    {
        // store gold locations and shuffle
        if (goldLocations.isEmpty()) {
            pillAndItemLocations = game.getPillAndItemLocations();
            for (Location location : pillAndItemLocations) {
                Color c = getBackground().getColor(location);
                if (c.equals(Color.yellow)) {
                    goldLocations.add(location);
                }
            }
            Collections.shuffle(goldLocations);
        }

        Location goldLocation = goldLocations.get(0);
        super.walkTowards(goldLocation);
        Color c = getBackground().getColor(getLocation());
        if (c.equals(Color.yellow)){
            goldLocations.remove(0);
        }
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

