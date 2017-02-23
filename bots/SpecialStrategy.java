package bots;

import java.util.*
import pirates.game.*
import java.lang.Math
import java.lang.reflect
import java.lang.class
import java.math


class SpecialStrategy implements Strategy {

    public void doTurn(PirateGame game, History history) {
        game.debug("Activated: SpecialStrategy");
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        List<Drone> enemyLivingDrones = game.getEnemyLivingDrones();
        List<City> enemyCities = game.getEnemyCities();
        int numMyLivingPirates = myLivingPirates.size();
        game.debug(numMyLivingPirates);
        //1
        for (int i = 0; i < numMyLivingPirates; i++) {
            if (!Attacker.tryAttack(myLivingPirates.get(i), game)) {
                int howManyOurscounter = 0;
                int howManyNonescounter = 0;
                int howManyEnemeyscounter = 0;
                for (int j = 0; j < game.getAllIslands().size(); j++) {
                    Player p = game.getAllIslands().get(j).owner;
                    if (p.id == 1) {
                        howManyOurscounter++;
                    }
                    if(p.id == -1){
                        howManyNonescounter++;
                    }
                    if(p.id == 0){
                        howManyEnemeyscounter++;
                    }
                }
                if (howManyOurscounter >= game.getAllIslands().size() - 1){//|| howManyNonescounter == game.getAllIslands().size()) {
                    Mover.moveAircraftToClosest(game.getMyLivingPirates().get(i), game.getEnemyLivingPirates(), game);
                //}else if(howManyEnemeyscounter >0){
                       // Mover.moveAircraft(myLivingPirates.get(i),game.getAllIslands().get(i%game.getAllIslands().size()),game);
                }else {
                    if (i % 2 == 0) {
                        Mover.moveAircraftToClosestToAnotherMapObject(
                                myLivingPirates.get(i),
                                enemyLivingDrones,
                                enemyCities.get(0),
                                game);
                    } else {
                        Mover.moveAircraftToClosest(myLivingPirates.get(i), enemyLivingDrones, game);
                    }
                }
            }
        }
        //2
        for (int i = 0; i < game.getMyLivingDrones().size(); i++) {
            Mover.moveAircraft(game.getMyLivingDrones().get(i), game.getMyCities().get(0), game);
        }
        //3
        game.debug(game.getAllIslands().get(0).owner.id);
    }
}
