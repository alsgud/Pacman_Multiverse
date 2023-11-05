package src;

import ch.aplu.jgamegrid.Location;

public class TX5 extends Monster{

    public TX5(Game game, MonsterType type) {
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
        Location pacLocation = game.pacActor.getLocation();
        super.walkTowards(pacLocation);
        angryApproach(getDirection());
    }

    public boolean canMove(Location location)
    {
        return super.canMove(location);
    }
    public void stopMoving(int seconds) {
        super.stopMoving(seconds);
    }

    public void setStopMoving(boolean stopMoving) {
        super.setStopMoving(stopMoving);
    }
}

