package bots;
import pirates.*;
import java.util.*;

class SpecialStrategy implements Strategy{
    private PirateGame game;
    private Player player;
    public SpecialStrategy(){}
    public void doTurn(PirateGame game , History history)
    {
        player = game.getMyself();
        this.game = game;
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        //game.debug(myLivingPirates.size());
        sendDecoy(myLivingPirates);
        decoyMechanics();
        //runAway(myLivingPirates);
        //game.debug(myLivingPirates.size());
        attack(myLivingPirates);
        sendToCities(myLivingPirates);
        sendToNotAsIslands(myLivingPirates);
        //sendToMyIsland(myLivingPirates); 
        sendDrones();
        
        
    }
    private void attack(List<Pirate> MyLivingPirates)
    {
        List<Pirate> ps = new ArrayList<>();//to delete all Attacker pirates
        for(Pirate p : MyLivingPirates)
        {
            int inRange = 0;
            for(Pirate enemy: game.getEnemyLivingPirates())
                if(enemy.inRange(p,game.getAttackRange()))
                    inRange++;
            if(inRange<2)
                if(Attacker.tryAttack(p,game))
                    ps.add(p);//need to remove
        }
        for(Pirate p : ps)//removing
        {
            MyLivingPirates.remove(p);
        }
        
    }
    private void sendToCities(List<Pirate> MyLivingPirates)
    {
        if(!MyLivingPirates.isEmpty())
        {
            List<Drone> dronesInRange = new ArrayList<>();
            
            
            for(City c : game.getNotMyCities()) 
            {
                for(Drone drone : game.getEnemyLivingDrones())
                    if(drone.inRange(c, 8))
                        dronesInRange.add(drone);
                Drone d = (Drone)Mover.getClosest(c,dronesInRange);
                Pirate p  = (Pirate)Mover.getClosest(d,MyLivingPirates);
                if(d!=null && p!=null)
                {
                    Mover.moveAircraft(p,d,game);
                }
                else
                {
                    p  = (Pirate)Mover.getClosest(c,MyLivingPirates);
                    if (p!=null){
                    Mover.moveAircraft(p,c,game);
                        
                    }
                }
                MyLivingPirates.remove(p);
            }
        }
    }
    private void sendDecoy(List<Pirate> MyLivingPirates) 
    {
        if(game.getMyself().turnsToDecoyReload!=0)
            return;
        int numOfMaxInRange = -1;
        Pirate needToDecoy = null;
        for(Pirate pirate : MyLivingPirates)
        {
            int numOfInRange = 0;
            for(Pirate enemy : game.getEnemyLivingPirates())
                if(enemy.inRange(pirate,game.getAttackRange()))
                    numOfInRange++;
            if(numOfInRange>numOfMaxInRange)
            {
                numOfMaxInRange = numOfInRange;
                needToDecoy = pirate;
            }
        }
        game.decoy(needToDecoy);
        MyLivingPirates.remove(needToDecoy);
        
        
    }
    /*
    * the decoy follows the closest airship to him, triggers it and then runns away
    * has a "runaway" function in it (not using the one from Mover)
    */
    private void decoyMechanics(){
        game.debug("here");
        Decoy decoy = player.decoy;
        int inRangeCounter = 0;
        Pirate inEnemyAttackRange = null;
        int[] random = {2,-2};
        int temp = (new Random()).nextInt(random.length);
        int delta = random[temp];
        //mabye -+1 on attackRange
        if (player.decoy != null && player.decoy.isAlive()){
            game.debug("alive");
        for (Pirate enemyPirate : game.getEnemyLivingPirates()){
            if (enemyPirate.inAttackRange(decoy.location)){
                inEnemyAttackRange = enemyPirate;
                inRangeCounter++;
            }
        }
        if (inRangeCounter > 0 && inEnemyAttackRange !=null){
            int attackRange = inEnemyAttackRange.attackRange;
            if(inEnemyAttackRange.distance(new Location(decoy.location.row + attackRange, decoy.location.col)) <=attackRange ||
               inEnemyAttackRange.distance(new Location(decoy.location.row - attackRange, decoy.location.col)) <=attackRange){
                Mover.moveAircraft(player.decoy,new Location(decoy.location.row,decoy.location.col +delta),0,game);
                
            }else if(inEnemyAttackRange.distance(new Location(decoy.location.row, decoy.location.col + attackRange)) <=attackRange||
                     inEnemyAttackRange.distance(new Location(decoy.location.row, decoy.location.col - attackRange)) <=attackRange){
                Mover.moveAircraft(player.decoy,new Location(decoy.location.row + delta,decoy.location.col),0,game);
            }
        }else{
            Mover.moveAircraftToClosest(player.decoy,game.getEnemyLivingPirates(),game);
            //Mover.moveAircraft(player.decoy,new Location(Pirate)Mover.getClosest(player.decoy));
        }
        }
        
    }
    private void sendToNotAsIslands(List<Pirate> MyLivingPirates)
    {
        if(!game.getNotMyIslands().isEmpty())
            for(int i = 0; i < MyLivingPirates.size();i++)
                Mover.moveAircraftToClosest(MyLivingPirates.get(i),game.getNotMyIslands(),game);
    }
    private void sendDrones()
    {
        for(Drone drone : game.getMyLivingDrones())
            Mover.moveAircraftToClosest(drone,game.getMyCities(),1,game);
        
    }
    
    
}
