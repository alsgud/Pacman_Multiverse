package src;

import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class Wizard extends Monster{

    public Wizard(Game game, MonsterType type) {
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
        boolean moved = false;
        // loop until valid move is found
        while (!moved) {
            int direction = randomiser.nextInt(8);
            turn(45 * direction);
            Location next;
            Location back;
            back = getLocation();
            next = getNextMoveLocation();
            // move to random direction if possible
            if (canMove(next)) {
                Color c = getBackground().getColor(next);
                setLocation(next);
                Location nextAdjacent = getNextMoveLocation();
                Color nextAdjC = getBackground().getColor(nextAdjacent);
                // if it's a blank cell, move
                if (c.equals(Color.lightGray)) {
                    return;
                }
                // if it's a jumpable wall, jump. otherwise randomise another move.
                if (c.equals(Color.gray)) {
                    setLocation(back);
                    if (nextAdjC.equals(Color.lightGray)) {
                        setLocation(nextAdjacent);
                        return;
                    }
                }
            }
        }
        angryApproach(getDirection());
    }
    // modified condition in case the Wizard is facing a jumpable wall
    public boolean canMove(Location location)
    {
        if (location.getX() >= game.getNumHorzCells() - 1
                || location.getX() < 1 || location.getY() >= game.getNumVertCells() - 1 || location.getY() < 1)
            return false;
        else
            return true;
    }
    public void setStopMoving(boolean stopMoving) {
        super.setStopMoving(stopMoving);
    }
}

