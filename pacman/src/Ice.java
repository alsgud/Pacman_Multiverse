package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class Ice extends Item{
    Ice(Game game) {
        super(game);
    }

    //polymorphic method that initialises ice actor object, places ice sprite on map and adds the object into iceCube arraylist
    public void putItem(GGBackground bg, Location location, Item item) {
        bg.setPaintColor(Color.blue);
        bg.fillCircle(game.toPoint(location), 5);
        Actor ice = new Actor("sprites/ice.png");
        item.iceCubes.add(ice);
        game.addActor(ice,location);
    }
}
