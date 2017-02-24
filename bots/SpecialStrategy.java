package bots;

import pirates.*;
import java.util.List;

class SpecialStrategy implements Strategy {

    private int howManyIslandsOurscounter;
    private int howManyIslandsEnemyscounter;
    private int howManyIslandsNonescounter;

    public void doTurn(PirateGame game, History history) {
        updateIslandsStatus(game);
        game.debug("Activated: SpecialStrategy");
        int numMyLivingPirates = game.getMyLivingPirates().size();
        for (int i = 0; i < numMyLivingPirates; i++) {
            if (!Attacker.tryAttack(game.getMyLivingPirates().get(i), game)) {
                // every else if here represents a different case \ situation
                if (howManyIslandsOurscounter >= game.getAllIslands().size()) {
                    Mover.moveAircraftToClosest(game.getMyLivingPirates().get(i), game.getEnemyLivingPirates(), game);
                    }

                }
            }
            //2
        }
            for (int i = 0; i < game.getMyLivingDrones().size(); i++) {
                Mover.moveAircraft(game.getMyLivingDrones().get(i), game.getMyCities().get(0), game);
            }
        }
    
    

    private void follow(Aircraft follower, Aircraft followed, PirateGame game) {
        Mover.moveAircraft(follower, followed.location, game);
    }

    private void attackDrones(PirateGame game, int i) {
        Mover.moveAircraftToClosestToAnotherMapObject(
                game.getMyLivingPirates().get(i),
                game.getEnemyLivingDrones(),
                game.getEnemyCities().get(0),
                game);
    }

    private void updateIslandsStatus(PirateGame game) {
        howManyIslandsOurscounter = 0;
        howManyIslandsEnemyscounter = 0;
        howManyIslandsNonescounter = 0;
        for (int j = 0; j < game.getAllIslands().size(); j++) {
            Player p = game.getAllIslands().get(j).owner;
            if (p.id == 1) {
                howManyIslandsOurscounter++;
            }
            if (p.id == -1) {
                howManyIslandsNonescounter++;
            }
            if (p.id == 0) {
                howManyIslandsEnemyscounter++;
            }
        }
