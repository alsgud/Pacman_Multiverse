package src;

import ch.aplu.jgamegrid.Location;

public class Troll extends Monster{

    public Troll(Game game, MonsterType type) {
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
        Location pacLocation = game.pacActor.getLocation();
        double oldDirection = getDirection();

        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next;
        setDirection(compassDir);

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
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
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

