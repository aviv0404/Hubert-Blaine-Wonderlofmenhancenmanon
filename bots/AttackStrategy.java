package bots;
import java.util.*
import pirates.game.*
import java.lang.Math
import java.lang.reflect
import java.lang.class
import java.math

class AttackStrategy implements Strategy {
    private static boolean isCentered = false;
    private static boolean[] isThere;
    private static boolean isIsland = false;
    private Player player;


    public void doTurn(PirateGame game, History history) {
        game.debug("Activated: AttackStrategy");
        //game.debug(game.getAllEnemyPirates().size());
        //game.debug(game.getAllMyPirates().size());
        player = game.getMyself();
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        List<Drone> myDrones = game.getMyLivingDrones();

        /*double[] centredLocationArray = getAvgOfPirates(game, myLivingPirates);
        Location centerLocation = new Location((int) centredLocationArray[0], (int) centredLocationArray[1]);

        game.debug("firstLocation.col");
        game.debug(centerLocation.col);
        game.debug("firstLocation.row");
        game.debug(centerLocation.row);
        game.debug(game.getMaxDronesCount());
        game.debug(game.getOpponentName());*/

        if (game.getAllEnemyPirates().size() > game.getAllMyPirates().size()) {
            //TODO
            game.debug("game.getAllEnemyPirates().size() > game.getAllMyPirates().size()");


            //move all the pirates in one way and move all the drones in the other way
            //one side of the island and the other side other island
            //decoy
            //List<Island> islandListNeutral = game.getNeutralIslands();
            //initBoolArray(myLivingPirates.size());
            for (int i = 0; i < myLivingPirates.size(); i++) {
                Pirate pirate = myLivingPirates.get(i);
                if (game.getNeutralIslands().size() > 0) {
                    Mover.moveAircraft(pirate, game.getNeutralIslands().get(0).location, game);
                } else {

                    Location pirateLocation = game.getMyCities().get(0).getLocation();
                    Location drownLocation;

                    if (game.getMyCities().get(0).getLocation().row == game.getAllIslands().get(0).getLocation().row) {
                        //if the same row so horizontal
                        game.debug("horiz");
                        pirateLocation = new Location(game.getAllEnemyPirates().get(0).getLocation().row - game.getAttackRange(), game.getAllEnemyPirates().get(0).getLocation().col - game.getAttackRange());
                    } else if (game.getMyCities().get(0).getLocation().col == game.getAllIslands().get(0).getLocation().col) {
                        //if the same col so vertical
                        game.debug("verti");

                        pirateLocation = new Location(game.getAllEnemyPirates().get(0).getLocation().row - game.getAttackRange(), game.getAllEnemyPirates().get(0).getLocation().col - game.getAttackRange());
                    }
                    if (pirate.isAlive()) {
                        if (Math.abs(pirate.getLocation().compareTo(pirateLocation)) < game.getAttackRange() + 2) {
                            if (player.turnsToDecoyReload == 0) {
                                game.decoy(game.getMyLivingPirates().get(i));
                                if (player.decoy != null && player.decoy.isAlive()) {
                                    Mover.moveAircraft(player.decoy, pirateLocation, game);
                                }
                            } else if (!Attacker.tryAttack(pirate, game)) {
                                Mover.moveAircraft(pirate, pirateLocation, game);
                            }
                        } else {
                            Mover.moveAircraft(pirate, pirateLocation, game);
                        }
                    }

                }
            }
            if (!myDrones.isEmpty()) {
                for (Drone drone : game.getMyLivingDrones()) {
                    Mover.moveAircraft(drone, game.getMyCities().get(0).location, game);
                }
            }
        }
    }
            /*
            for (int i = 0; i < myLivingPirates.size(); i++) {
                Pirate p = myLivingPirates.get(i);
                if (!Attacker.tryAttack(p, game)) {
                        Mover.moveAircraft(p, game.getMyCities().get(0).getLocation(), game);
                }
            }*/

}
/*
else if(game.getAllEnemyPirates().

        size()

        ==game.getAllMyPirates().

        size()

        )

        {
            /*
            //TODO

            //List<Island> islandListNeutral = game.getNeutralIslands();

            //go and get the Islands
            initBoolArray(myLivingPirates.size());
            if (game.getNeutralIslands().size() > 0) {
                game.debug("1");
                if (myLivingPirates.size() != 0) {
                    game.debug("2");
                    for (int i1 = 0; i1 < myLivingPirates.size(); i1++) {
                        Pirate p = myLivingPirates.get(i1);
                        if (isCentered) {
                            game.debug("3");
                            if (p.isAlive() && game.getNeutralIslands().get(0).distance(p) > game.getControlRange()) {
                                Mover.moveAircraft(p, game.getNeutralIslands().get(0).getLocation(), game);
                            }
                        } else {
                            //if every body at the same point isCentered = true;
                            //else // move everybody to the same point
                            game.debug("4");
                            if (p.isAlive()) {
                                game.debug("5");
                                Mover.moveAircraft(p, centerLocation, game);
                            }
                            isThere[i1] = p.getLocation().compareTo(centerLocation) == 0;
                        }
                    }
                    if (areAllTrue(isThere)) {
                        game.debug("6");
                        isCentered = true;
                    }
                }

            } else if (myLivingPirates.size() != 0) {
                if (isCentered) {
                    for (int i = 0; i < myLivingPirates.size(); i++) {
                        Pirate p = myLivingPirates.get(i);
                        Pirate pirate = game.getAllEnemyPirates().get(i);

                        game.debug(p.currentHealth);
                        game.debug(pirate.currentHealth);

                        if (!Attacker.tryAttack(p, game)) {
                            if (myDrones.size() == game.getMaxDronesCount())
                                Mover.moveAircraft(p, game.getMyCities().get(0).getLocation(), game);
                        }
                    }

                }
            }

            if (myLivingPirates.size() == 3) {
                //game.debug(game.getMaxDronesCount());

                //move drones
                if (myDrones.size() == game.getMaxDronesCount()) {

                    for (Drone drone : game.getMyLivingDrones()) {
                        if (myLivingPirates.size() == 3) {
                            Mover.moveAircraft(drone, game.getMyCities().get(0).location, game);
                        } else {
                            Mover.moveAircraft(drone, new Location(game.getMyCities().get(0).location.row, game.getMyCities().get(0).location.col - 10), game);
                        }
                    }
                }
            }

            /*for(Pirate pirate:game.getAllMyPirates()){
                if (!pirate.isAlive()){
                    numOfDeathPirate++;
                }
            }

            if (numOfDeathPirate == game.getAllMyPirates().size()){
                numOfDeathPirate = 0;
                isCentered = false;
                initBoolArray(game.getAllMyPirates().size());
            }


        }

        else if(game.getAllEnemyPirates().

        size()

<game.

        getAllMyPirates()

        .

        size()

        )

        {
        //TODO
        game.debug("game.getAllEnemyPirates().size() < game.getAllMyPirates().size()");
        }


        }

    /*public static boolean areAllTrue(boolean[] array) {
        for (boolean b : array) if (!b) return false;
        return true;
    }

    private static double[] getAvgOfPirates(PirateGame game, List<Pirate> pirates) {
        double sumC = 0;
        double sumR = 0;
        for (Pirate p : pirates) {
            sumC += p.initialLocation.col;
            sumR += p.initialLocation.row;
        }
        double avrC = sumC / pirates.size();

        game.debug("avrC");
        game.debug(avrC);
        double avrR = sumR / pirates.size();
        game.debug("avrR");
        game.debug(avrR);

        return new double[]{avrR, avrC};
    }

    private static void initBoolArray(int size) {

        isThere = new boolean[size];
        if (!isCentered) {
            for (int i = 0; i < isThere.length; i++) {
                isThere[i] = false;
            }
        }
    }*/

