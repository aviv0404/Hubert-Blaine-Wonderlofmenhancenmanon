package bots;

import pirates.*;
import java.util.*;

class AttackStrategy implements Strategy
{

    private boolean isCentered;
    private boolean[] isThere;
    private Player p;

    public AttackStrategy(){}
    
    public AttackStrategy(boolean[] isThere)
    {
        this.isThere = isThere;
    }

    public void doTurn(PirateGame game, History history)
    {
        game.debug(game.getTurn());
        //isThere = new boolean[game.getMaxDronesCount()];
        /*
        if(game.getTurn() == 1){
            game.debug("constructor");
            isCentered = false;
            isThere = new boolean[game.getMaxDronesCount()];
            for(int i = 0; i < game.getMaxDronesCount(); i++)
                {
                    isThere[i] = false;
                    game.debug(isThere);
                }
        }*/
        p = game.getMyself();
        game.debug("Activated: AttackStrategy");
        Location center = new Location(game.getRowCount() / 2, game.getColCount() / 2);
        List<Pirate> myLivingPirates = game.getMyLivingPirates();
        List<Drone> myLivingDrones = game.getMyLivingDrones();
        
        game.debug(isCentered);
        isCentered = areAllTrue(isThere);
        
        if(!game.getNeutralIslands().isEmpty())
        {

            game.debug("1");
            for(int i = 0; i < myLivingPirates.size(); i++)
            {
                Pirate pirate = myLivingPirates.get(i);
                Mover.moveAircraft(pirate, game.getNeutralIslands().get(0), game);
            }
        }
        else if(game.getTurn() < 250)
        {
            game.debug("2");
            ///
            for(int i = 0; i < myLivingPirates.size(); i++)
            {
                Pirate pirate = myLivingPirates.get(i);
                Mover.moveAircraft(pirate, center, game);

            }
            game.debug(myLivingDrones.size());
            for(int i = 0; i < myLivingDrones.size(); i++)
            {
                
                Drone drone = myLivingDrones.get(i);
                Mover.moveAircraft(drone, center, game);
                if(drone.getLocation().compareTo(center) == 0)
                {
                    isThere[i] = true;
                    //game.debug(isThere[i]);
                }
            }
            isCentered = areAllTrue(isThere);
            
        //game.debug("is1");
        //game.debug(isCentered);
        
        //game.debug(isCentered);
        //game.debug("is2");    
            ////
        }else{
            game.debug("3");
            for(int i = 0; i < myLivingDrones.size(); i++)
            {
                Drone drone = myLivingDrones.get(i);
                Mover.moveAircraft(drone, game.getMyCities().get(0).getLocation(), game);
            }
            
            for(int i = 0; i < myLivingPirates.size(); i++)
            {
                Pirate pirate = myLivingPirates.get(i);
                /*if(p.turnsToDecoyReload == 0 && i == 0)
                {
                   

                else */
                if(!Attacker.tryAttack(pirate, game))
                {
                    Drone closest = (Drone)Mover.getClosest(pirate.location,game.getMyLivingDrones());
                    Mover.moveAircraft(pirate, new Location(closest.getLocation().row - 1,closest.getLocation().col - 3), game);
                }

            }
            
        
        game.debug("isCentered");
        game.debug(isCentered);
        //end of the turn
        /*for(int i = 0; i < game.getMaxDronesCount(); i++)
            {
                  
            }*/
            
        }
    }

    private void initBoolArray(int size)
    {
        isThere = new boolean[size];
        if(!isCentered)
        {
            for(int i = 0; i < size; i++)
            {
                isThere[i] = false;
            }
        }
    }

    private boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array)
        {
        
            if(!b)
            {
                return false;
            }
        }
        return true;
    }
}
