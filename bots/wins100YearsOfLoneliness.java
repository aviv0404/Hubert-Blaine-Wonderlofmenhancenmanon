package bots;

import pirates.*;
import java.util.*;

class wins100YearsOfLoneliness implements Strategy{
    private PirateGame game;
    private Player player;
    public void doTurn(PirateGame game, History history){
        player = game.getMyself();
        this.game = game;
        game.debug("Activated: wins100YearsOfLoneliness");
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        sendDrones();
        sendToIsland0(myLivingPirates);
        defender(4);

        

    }
    
     private void defender(int id){
        Pirate p4 = game.getMyPirateById(id);
        if (!Attacker.tryAttack(p4,game)){
        if (!game.getEnemyLivingDrones().isEmpty()){
            Mover.moveAircraftToClosest(p4,game.getEnemyLivingDrones(),game);
            game.debug("if");
        }else{
            game.debug("else");
            Mover.moveAircraft(p4,game.getNeutralCities().get(0),game);
        }
            
        }
    }
    
    private void sendToCities(List<Pirate> MyLivingPirates) {
        if (!MyLivingPirates.isEmpty()) {
            List<Drone> dronesInRange = new ArrayList<>();

            for (City c : game.getNeutralCities()) {
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
    
    private void sendToIsland0(List<Pirate> myLivingPirates){
        Pirate p0 = game.getMyPirateById(0);
        Pirate p1 = game.getMyPirateById(1);
        Pirate p2 = game.getMyPirateById(2);
        Pirate p3 = game.getMyPirateById(3);
        
        if (p0.isAlive() && !Attacker.tryAttack(p0,game) ){
            Mover.moveAircraft(p0,game.getAllIslands().get(0),game);
        }
        if (p1.isAlive() && !Attacker.tryAttack(p1,game) ){
            Mover.moveAircraft(p1,game.getAllIslands().get(0),game);
        }
        if (p2.isAlive() && !Attacker.tryAttack(p2,game) ){
            Mover.moveAircraft(p2,game.getAllIslands().get(0),game);
        }
        if (p3.isAlive() && !Attacker.tryAttack(p3,game) ){
            Mover.moveAircraft(p3,game.getAllIslands().get(0),game);
        }
        
        
        myLivingPirates.remove(p0);
        myLivingPirates.remove(p1);
        myLivingPirates.remove(p2);
        myLivingPirates.remove(p3);
        
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
    
    private void sendDrones() {
        boolean didHeAttack = false;
        boolean didHeMove = false;
         for (Drone drone : game.getMyLivingDrones()) {
            Location destination0 = new Location(24, 44);
            if(drone.getLocation().row!=destination0.row){
                Mover.moveAircraft(drone, destination0, game);
            }
            else{
                Mover.moveAircraftToClosest(drone, game.getNeutralCities(), 1, game);
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
}