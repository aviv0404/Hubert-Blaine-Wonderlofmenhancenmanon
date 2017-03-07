package bots;

import pirates.*;
import java.util.*;

class winsGiberLetter implements Strategy{
    
    private PirateGame game;
    
    public void doTurn(PirateGame game, History history){
        this.game = game;
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        sendDrones();
        sendToAttackDronesStupid(myLivingPirates);
        sendToNotAsIslands(myLivingPirates);
        game.debug("Activated: GiberLetter");
    }
    
    private void sendToAttackDronesStupid(List<Pirate> myLivingPirates){
        boolean didHeAttack = false;
        boolean didHeMove = false;
         if (game.getMyPirateById(3).isAlive()){
            Pirate myPirate = game.getMyPirateById(3);
            if(Attacker.tryAttackDrones(myPirate, game)){
                didHeAttack =true;
            }else{
                Location destination0 = new Location(16,1);
                Location destination1 = new Location(12,1);
                if(myPirate.getLocation().col!=destination0.col){
                    Mover.moveAircraft(myPirate,destination0, game);
                }
                else{
                    Mover.moveAircraft(myPirate,destination1, game);
                }
                
                didHeMove=true;
            }
            if (!didHeAttack&&!didHeMove){
                //Mover.runAway(myPirate,Mover.getClosest(myPirate,game.getEnemyLivingPirates()),5,game);
            }
        }
        myLivingPirates.remove(game.getMyPirateById(3));
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