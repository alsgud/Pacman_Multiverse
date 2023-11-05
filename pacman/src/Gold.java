package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class Gold extends Item {
    Gold (Game game) {
        super(game);
    }

    //polymorphic method that initialises gold actor object, places gold sprite on map, and adds object into goldPieces arraylist
    public void putItem(GGBackground bg, Location location, Item item) {
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(game.toPoint(location), 5);
        Actor gold = new Actor("sprites/gold.png");
        item.goldPieces.add(gold);
        game.addActor(gold,location);
    }
}
