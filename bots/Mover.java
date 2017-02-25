package bots;
import java.util.*
import pirates.game.*
import java.lang.Math
import java.lang.reflect
import java.lang.class
import java.math


class Mover {
    /**
     * Moves the aircraft towards the destination.
     *
     * @param aircraft - the aircraft to move
     * @param destination - the destination of the aircraft
     * @param game - the current game state
     */
    static void moveAircraft(Pirate pirate, MapObject destination, PirateGame game) {
        // Get sail options
        List<Location> sailOptions = game.getSailOptions(pirate, destination);
        // Set sail!
        game.setSail(pirate, sailOptions.get(0));
    }
    
    
    
     /**
     * Moves the aircraft towards the destination.
     *
     * @param aircraft - the aircraft to move
     * @param targets - a list of target thet we want to go to the closer of them
     * @param game - the current game state
     *
     *return - if aircraft moved
     *
     */
    
    //TODO: Think of a shorter name
    static <T extends MapObject> boolean moveAircraftToClosest(Pirate pirate, List<T> targets, PirateGame game)
    {   
        //first closer location is null location
        Location closest = null;
        for(T target:targets)
        {
            if(closest==null || aircraft.distance(closest)>pirate.distance(target))
                closest = target.getLocation();
        }
        
        if(closest==null || pirate.distance(closest)==0)//no targets or Aircraft not need to move
            return false;
        
        moveAircraft(pirate,closest,game);
        return true;
   }
    
    
    
    //TODO: Think of a shorter name
    static <T extends MapObject> boolean moveAircraftToClosestToAnotherMapObject(Pirate pirate, List<T> targets, MapObject mapObject,PirateGame game)
    {   
        //first closer location is null location
        Location closest = null;
        for(T target:targets)
        {
            if(closest==null || mapObject.distance(closest)>mapObject.distance(target))
                closest = target.getLocation();
        }
        
        if(closest==null || pirate.distance(closest)==0)//no targets or Aircraft not need to move
            return false;
        
        moveAircraft(pirate,closest,game);
        return true;
   }
    
}
