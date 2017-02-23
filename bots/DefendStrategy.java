package bots;

import java.util.*
import pirates.game.*
import java.lang.Math
import java.lang.reflect
import java.lang.class
import java.math


class DefendStrategy implements Strategy {

    
    //TODO: mabye change myCities.get(0) if there's more than one city
    @Override
    public void doTurn(PirateGame game, History history) {
        game.debug("Activated: DefendStrategy");
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        List<Drone> enemyLivingDrones = game.getEnemyLivingDrones();
        List<City> enemyCities = game.getEnemyCities();
        int numMyLivingPirates = myLivingPirates.size();
        game.debug(numMyLivingPirates);
        for (int i = 0; i < numMyLivingPirates; i++) {
            if (!Attacker.tryAttack(myLivingPirates.get(i), game)) {
                Mover.moveAircraftToClosestToAnotherMapObject(
                                myLivingPirates.get(i),
                                enemyLivingDrones,
                                enemyCities.get(0),
                                game);
            }
        }
    }
}
