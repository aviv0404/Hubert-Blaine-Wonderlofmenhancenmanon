package bots;

import pirates.*;
import java.util.*;

class SpecialStrategy implements Strategy {

    private PirateGame game;
    private Player player;
    private final History history;
    private final Mover mover;

    public SpecialStrategy(History history) {
        this.history = history;
        mover = new Mover(0);
    }

    public void doTurn(PirateGame game, History history) {
        List< Pirate> myLivingPirates = game.getMyLivingPirates();
        
        game.debug("Activated: SpecialStrategy");
        player = game.getMyself();
        this.game = game;
        sendDecoy(myLivingPirates);
        decoyMechanics();
        prioritiesActions(myLivingPirates);
        //smartDrones();
        sendDrones();
        smartSailOptions();

        //game.debug(myLivingPirates.size());
    }

    //changes sailOptions from 0 to 1 whenever we repeat ourselves, changes it back after n turns
    private void smartSailOptions(){
        if (history.doWeRepeat(4, 2)){// 1 4 | 4 2 | 3 4 |
            mover.setSailOption(1);
        }else{
            mover.setSailOption(0);
        }
    }
    //TODO: decide when we should defend Cities or attack islands or do anything else, to make it smarter
    // sendToCities HAS to be on top of sendToNotAsIslands or else it will not work properly
    private void prioritiesActions(List<Pirate> myLivingPirates) {
        evade(myLivingPirates);
        if (!game.getNotMyIslands().isEmpty()) {
            defendCities(myLivingPirates);
            sendToNotAsIslands(myLivingPirates);
        } else {
            //defendMyIslands(myLivingPirates);
            attack(myLivingPirates);
        }
    }

    private void defendMyIslands(List<Pirate> myLivingPirates) {
        ArrayList<Pirate> ps = new ArrayList<>();
        if (!game.getMyIslands().isEmpty()) {
            for (Island island : game.getMyIslands()) {
                Pirate pirate = (Pirate) Mover.getClosest(island, myLivingPirates);
                if (!Attacker.tryAttack(pirate, game)) {
                    Mover.moveAircraft(pirate, island, game);
                    ps.add(pirate);
                }
            }
        }
        for (Pirate pirate : ps) {
            myLivingPirates.remove(pirate);
        }
    }

    private void smartDrones() {
        if (!game.getMyLivingDrones().isEmpty() && !game.getEnemyLivingPirates().isEmpty()) {
            for (Drone drone : game.getMyLivingDrones()) {
                Pirate pirate = (Pirate) Mover.getClosest(drone, game.getEnemyLivingPirates());
                if (pirate.inAttackRange(drone)) {
                    int[] random = {
                        2,
                        -2
                    };
                    int temp = (new Random()).nextInt(random.length);
                    int delta = random[temp];
                    Mover.runAway(drone, pirate, delta, game);
                    //you could do -
                    //Mover.runAway(drone, pirate, 3 * Mover.getDirectionOf(drone, (City) Mover.getClosest(drone, game.getMyCities())), game);
                } else {
                    if (!game.getMyCities().isEmpty()) {
                        Mover.moveAircraftToClosest(drone, game.getMyCities(), game);
                    } else {
                        Mover.moveAircraftToClosest(drone, game.getNeutralCities(), game);
                    }
                }
            }
        }
    }

    /*
     * escapes to the nearest city the moment you are in a disadvantage
     */
    private boolean evade(List<Pirate> myLivingPirates) {
        List<Aircraft> enemyPiratesInRange = new ArrayList<>();
        List<Aircraft> myPiratesInRange = new ArrayList<>();
        Iterator<Pirate> iterMy = myLivingPirates.iterator();
        Iterator<Pirate> iterAllies = game.getMyLivingPirates().iterator();
        Iterator<Pirate> iterEnemy = game.getEnemyLivingPirates().iterator();

        while (iterMy.hasNext()) {
            int counterEnemy = 0;
            int counterAlly = 0;
            Pirate me = iterMy.next();
            while (iterEnemy.hasNext()) {
                Pirate enemy = iterEnemy.next();
                if (me.inAttackRange(enemy)) {
                    //this - enemyPiratesInRange.add(enemy); is useless right now
                    enemyPiratesInRange.add(enemy);
                    counterEnemy++;

                }
            }
            while (iterAllies.hasNext()) {
                Pirate myPirate = iterAllies.next();
                if (me.inAttackRange(myPirate)) {
                    //this - myPiratesInRange.add(myPirate); is useless right now
                    myPiratesInRange.add(myPirate);
                    counterAlly++;

                }
            }
            if (counterEnemy > counterAlly) {
                //Mover.goToTheOppositeDirectionOf(me,piratesInRange,5,game);
                Mover.moveAircraftToClosest(me, game.getMyCities(), game);
                iterMy.remove();
                return true;
            }
        }
        return false;

    }

    private void attack(List< Pirate> MyLivingPirates) {
        List< Pirate> ps = new ArrayList<>(); //to delete all Attacker pirates
        for (Pirate p : MyLivingPirates) {
            int inRange = 0;
            for (Pirate enemy : game.getEnemyLivingPirates()) {
                if (enemy.inRange(p, game.getAttackRange())) {
                    inRange++;
                }
            }
            if (inRange < 2) {
                if (!Attacker.tryAttack(p, game)) {
                    Mover.moveAircraftToClosest(p, game.getEnemyLivingPirates(), game);
                }
            }
        }
        for (Pirate p : ps) //removing
        {
            MyLivingPirates.remove(p);
        }

    }

    private void defendCities(List<Pirate> mylivingPirates) {
        Pirate closestPirateToCity = null;
        for (City city : game.getNotMyCities()) {
            if (!game.getEnemyLivingDrones().isEmpty()) {
                Drone closestDroneToCity = (Drone) Mover.getClosest(city, game.getEnemyLivingDrones());
                closestPirateToCity = (Pirate) Mover.getClosest(city, mylivingPirates);
                
                if (closestDroneToCity != null && closestPirateToCity != null) {
                    if (closestPirateToCity.distance(city) / 2 < closestDroneToCity.distance(city)) {
                        if (Attacker.tryAttackDrones(closestPirateToCity, game)) {
                            break;
                        }else{
                            Mover.moveAircraft(closestPirateToCity, closestDroneToCity, 1, game);
                            break;
                        }
                    }
                }
            }
        }
        if (closestPirateToCity != null) {
            mylivingPirates.remove(closestPirateToCity);
        }
    }

    private void sendToCities(List<Pirate> MyLivingPirates) {
        if (!MyLivingPirates.isEmpty()) {
            List<Drone> dronesInRange = new ArrayList<>();

            for (City c : game.getNotMyCities()) {
                for (Drone drone : game.getEnemyLivingDrones()) {
                    if (drone.inRange(c, 8)) {
                        dronesInRange.add(drone);
                    }
                }
                Drone d = (Drone) Mover.getClosest(c, dronesInRange);
                Pirate p = (Pirate) Mover.getClosest(d, MyLivingPirates);
                if (!game.getEnemyLivingDrones().isEmpty()) {
                    p = (Pirate) Mover.getClosest(c, MyLivingPirates);
                    if (p != null) {
                        Mover.moveAircraft(p, c, game);
                    }

                    if (d != null && p != null && !Attacker.tryAttack(p, game)) {
                        int[] direction = Mover.getDirectionOf(p, d);
                        Location cityLocation = c.getLocation();
                        //direction[0] == row
                        //direction[1] == col
                        //right
                        if (direction[0] == 0 && direction[1] == 1) {
                            Mover.moveAircraft(p, new Location(cityLocation.row, cityLocation.col + c.unloadRange), game);
                            //up
                        } else if (direction[0] == 1 && direction[1] == 0) {
                            Mover.moveAircraft(p, new Location(cityLocation.row - c.unloadRange, cityLocation.col), game);
                            //down
                        } else if (direction[0] == -1 && direction[1] == 0) {
                            Mover.moveAircraft(p, new Location(cityLocation.row + c.unloadRange, cityLocation.col), game);
                            //left
                        } else if (direction[0] == 0 && direction[1] == -1) {
                            Mover.moveAircraft(p, new Location(cityLocation.row, cityLocation.col - c.unloadRange), game);

                        }
                    }
                }
                MyLivingPirates.remove(p);
            }
        }
    }

    private void sendDecoy(List< Pirate> MyLivingPirates) {
        game.debug("decoy spawns in:" + game.getMyself().turnsToDecoyReload + " turns");
        if (game.getMyself().turnsToDecoyReload != 0) {
            return;
        }
        int numOfMaxInRange = -1;
        Pirate needToDecoy = null;
        for (Pirate pirate : MyLivingPirates) {
            int numOfInRange = 0;
            for (Pirate enemy : game.getEnemyLivingPirates()) {
                if (enemy.inRange(pirate, game.getAttackRange())) {
                    numOfInRange++;
                }
            }
            if (numOfInRange > numOfMaxInRange) {
                numOfMaxInRange = numOfInRange;
                needToDecoy = pirate;
            }
        }
        if (numOfMaxInRange > 0) {
            game.decoy(needToDecoy);
            MyLivingPirates.remove(needToDecoy);
        }

    }
    /*
     * the decoy follows the closest airship to him, triggers it and then runns away
     * has a "runaway" function in it (not using the one from Mover)
     */

    private void decoyMechanics() {
        Decoy decoy = player.decoy;
        int inRangeCounter = 0;
        Pirate inEnemyAttackRange = null;
        int[] random = {
            2,
            -2
        };
        //if(){}else{}
        int temp = (new Random()).nextInt(random.length);
        int delta1 = random[temp];
        int delta = -2;
        //mabye -+1 on attackRange
        if (player.decoy != null && player.decoy.isAlive()) {
            for (Pirate enemyPirate : game.getEnemyLivingPirates()) {
                if (enemyPirate.inAttackRange(decoy.location)) {
                    inEnemyAttackRange = enemyPirate;
                    inRangeCounter++;
                }
            }

            if (inRangeCounter > 0 && inEnemyAttackRange != null) {
                int attackRange = inEnemyAttackRange.attackRange;
                Mover.runAway(player.decoy, inEnemyAttackRange, -2, game);
            } else {
                Mover.moveAircraftToClosest(player.decoy, game.getEnemyLivingPirates(), game);
                //Mover.moveAircraft(player.decoy,new Location(Pirate)Mover.getClosest(player.decoy));
            }
        }

    }

    private void sendToNotAsIslands(List< Pirate> MyLivingPirates) {
        if (game.getNeutralCities().isEmpty()) {
            if (!game.getNotMyIslands().isEmpty()) {
                for (Pirate pirate : MyLivingPirates) {
                    if (!Attacker.tryAttack(pirate, game)) {
                        Mover.moveAircraftToClosest(pirate, game.getNotMyIslands(), game);
                    }

                }
            }
        } else if (!game.getNotMyIslands().isEmpty() && !MyLivingPirates.isEmpty()) {
            for (Pirate pirate : MyLivingPirates) {
                //MapObject c = Mover.getClosest(game.getNeutralCities().get(0), game.getNotMyIslands());
                if (!Attacker.tryAttack(pirate, game)) {
                    Mover.moveAircraftToClosest(pirate, game.getNotMyIslands(), game);
                }
            }
        }

    }

    private void sendDrones() {
        for (Drone drone : game.getMyLivingDrones()) {
            if (game.getMyCities().isEmpty()) {
                Mover.moveAircraftToClosest(drone, game.getNeutralCities(), 1, game);
            } else if (game.getMyCities().size() > 0 && game.getNeutralCities().size() > 0) {
                Mover.moveAircraftToClosest(drone, game.getNeutralCities(), 1, game);
            } else {
                Mover.moveAircraftToClosest(drone, game.getMyCities(), 1, game);
            }

        }

    }

}
