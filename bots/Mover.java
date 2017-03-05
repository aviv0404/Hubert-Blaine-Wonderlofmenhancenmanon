package bots;

import java.util.ArrayList;
import java.util.List;
import pirates.*;

class Mover {
    
    protected static int sailOption = 0;

    /**
     * Moves the aircraft towards the destination.
     *
     * @param aircraft - the aircraft to move
     * @param destination - the destination of the aircraft
     * @param game - the current game state
     */
     
    public Mover(int sailOption){
        Mover.sailOption = sailOption;
    }
    public void setSailOption(int n){
        sailOption = n;
    }
     
    static void moveAircraft(Aircraft aircraft, MapObject destination, int i, PirateGame game) {
        // Get sail options
        List<Location> sailOptions = game.getSailOptions(aircraft, destination);
        // Set sail!
        while (i >= sailOptions.size()) {
            i--;
        }
        game.setSail(aircraft, sailOptions.get(i));
    }

    static void moveAircraft(Aircraft aircraft, MapObject destination, PirateGame game) {
        // Get sail options
        moveAircraft(aircraft, destination, sailOption, game);
    }

    static <T extends MapObject> MapObject getClosest(MapObject mapObject, List<T> list) {
        if (mapObject == null)//no target
        {
            return null;
        }

        MapObject closest = null;
        for (T t : list) {
            if (closest == null || closest.distance(mapObject) > t.distance(mapObject)) {
                closest = t;
            }
        }
        return closest;
    }
    /*
     * @param attackRange enemy's attackRange
     */

    static void runAway(Aircraft runner, Pirate runFrom, int delta, PirateGame game) {
        int attackRange = runFrom.attackRange;
        if (runFrom.distance(new Location(runner.location.row + attackRange, runner.location.col)) <= attackRange + 1
                || runFrom.distance(new Location(runner.location.row - attackRange, runner.location.col)) <= attackRange + 1) {
            Mover.moveAircraft(runner, new Location(runner.location.row, runner.location.col + delta), 0, game);

        } else if (runFrom.distance(new Location(runner.location.row, runner.location.col + attackRange)) <= attackRange + 1
                || runFrom.distance(new Location(runner.location.row, runner.location.col - attackRange)) <= attackRange + 1) {
            Mover.moveAircraft(runner, new Location(runner.location.row + delta, runner.location.col), 0, game);
        }
    }
    /*
     * +1 - down || right
     * -1 - top || left
     * 0 - you are at your destination already
     * mainly use if for runAway if you wanna run to the direction of somthing
     */

    static int[] getDirectionOf(MapObject from, MapObject towards) {
        Location fromL = from.getLocation();
        Location towardL = towards.getLocation();
        int col = 0;
        int row = 0;
        //right
        if (fromL.col>towardL.col){
            col = 1;
        //left
        }else if (fromL.col < towardL.col){
           col = -1;
        }
        //down
        if(fromL.row > towardL.row){
            row = 1;
        //up
        }else if (fromL.row < towardL.row){
            row = -1;
        }
        return new int[]{row,col};
    }

    /*
     * not tested yet
     */
    static void goToTheDirectionOf(Aircraft mover, MapObject mapObject, PirateGame game) {
        Location mapObjectLoc = mapObject.getLocation();
        if (mover.location.col != mapObjectLoc.col) {
            moveAircraft(mover, new Location(mover.location.row, mapObjectLoc.col), game);
        } else if (mover.location.row != mapObjectLoc.row) {
            moveAircraft(mover, new Location(mapObjectLoc.row, mover.location.col), game);
        }
    }
    /*
     * not tested yet
     */

    static void goToTheOppositeDirectionOf(Aircraft mover, List<Aircraft> from, int delta, PirateGame game) {
        /*
         Location mapObjectLoc = mapObject.getLocation();
         if (mover.location.col != mapObjectLoc.col){
         moveAircraft(mover,new Location(mover.location.row,-mapObjectLoc.col),game);
         }else if(mover.location.row != mapObjectLoc.row){
         moveAircraft(mover,new Location(-mapObjectLoc.row,mover.location.col),game);
         }
         */
        int sumY = 0;
        int sumX = 0;
        List<Location> locations = new ArrayList<>();

        for (Aircraft ac : from) {
            locations.add(ac.getLocation());
        }

        for (Location targetLocation : locations) {
            sumY += targetLocation.row;
            sumX += targetLocation.col;
        }
        Location moverLocation = mover.getLocation();
        Location runFrom;
        if (!locations.isEmpty()) {
            runFrom = new Location(sumY / locations.size(), sumX / locations.size());
            if (runFrom.col - moverLocation.col != 0) {
                int m = (runFrom.row - moverLocation.row) / (runFrom.col - moverLocation.col);
                if (y(m, moverLocation.col + delta, runFrom.col, runFrom.row) <= game.getRowCount()
                        && x(m, moverLocation.row + delta, runFrom.row, runFrom.col) <= game.getColCount()) {
                    moveAircraft(mover, new Location(y(m, moverLocation.col + delta, runFrom.col, runFrom.row),
                            x(m, moverLocation.row + delta, runFrom.row, runFrom.col)), game);
                    Location l = new Location(y(m, moverLocation.col + delta, runFrom.col, runFrom.row),
                            x(m, moverLocation.row + delta, runFrom.row, runFrom.col));
                    game.debug(l);
                }else{
                    game.debug("choose a different delta");
                }
            }
        }
    }

    private static int y(int m, int x, int x1, int y1) {
        return (m * (x - x1)) + y1;
    }

    private static int x(int m, int y, int y1, int x1) {
        if (m != 0) {
            return (y + (m * x1) - y1) / m;
        }
        return 0;
    }

    /**
     * Moves the aircraft towards the destination.
     *
     * @param aircraft - the aircraft to move
     * @param targets - a list of target that we want to go to the closer of
     * them
     * @param game - the current game state
     *
     * return - if aircraft moved
     *
     */
    //TODO: Think of a shorter name
    static <T extends MapObject> boolean moveAircraftToClosest(Aircraft aircraft, List<T> targets, PirateGame game) {
        //first closer location is null location
        Location closest = null;
        for (T target : targets) {
            if (closest == null || aircraft.distance(closest) > aircraft.distance(target)) {
                closest = target.getLocation();
            }
        }

        if (closest == null || aircraft.distance(closest) == 0) {//no targets or Aircraft not need to move
            return false;
        }

        moveAircraft(aircraft, closest, game);
        return true;
    }

    static <T extends MapObject> boolean moveAircraftToClosest(Aircraft aircraft, List<T> targets, int i, PirateGame game) {
        //first closer location is null location
        Location closest = null;
        for (T target : targets) {
            if (closest == null || aircraft.distance(closest) > aircraft.distance(target)) {
                closest = target.getLocation();
            }
        }

        if (closest == null || aircraft.distance(closest) == 0) {//no targets or Aircraft not need to move
            return false;
        }

        moveAircraft(aircraft, closest, i, game);
        return true;
    }

    //TODO: Think of a shorter name
    static <T extends MapObject> boolean moveAircraftToClosestToAnotherMapObject(Aircraft aircraft, List<T> targets, MapObject mapObject, PirateGame game) {
        //first closer location is null location
        Location closest = null;
        for (T target : targets) {
            if (closest == null || mapObject.distance(closest) > mapObject.distance(target)) {
                closest = target.getLocation();
            }
        }

        if (closest == null || aircraft.distance(closest) == 0) {//no targets or Aircraft not need to move
            return false;
        }

        moveAircraft(aircraft, closest, game);
        return true;
    }

}
