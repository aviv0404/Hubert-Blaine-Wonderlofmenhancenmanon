package bots;
import pirates.*;

/**
 * This is an example for a bot.
 */
public class MyBot implements PirateBot {

    /**
     * Makes the bot run a single turn
     *
     * @param game - the current game state.
     * 
     */
     
    @Override
    public void doTurn(PirateGame game) {
        /*if(firstTurn()) {
            initFirstTurn();
        }
        
        saveHistory();
        Strategy strategy = decideStrategy();
        strategy.doTurn(game);
        */
        if (game.getEnemyCities().size() == 0){
            attack();
        }
        if (game.getMyCities().size() == 0){
            defense();
        }
        if (game.getMyCities().size() > && game.getEnemyCities().size() > 0){
            specialStrategy();
        }
    }

    
    private void defense(){
        
    }
    
    //1 pirate that sends drones and the other attack
    private void attack(){
        
    }
    
    private void specialStrategy(){
        
    }
    /**
     * Gives orders to my pirates
     *
     * @param game - the current game state
     */
    private void handlePirates(PirateGame game) {
        // Go over all of my pirates
        for (Pirate pirate : game.getMyLivingPirates()) {
            // Try to attack, if you didn't - move to an island
            if (!Attacker.tryAttack(pirate, game)) {
                // Choose destination
                Island destination = game.getAllIslands().get(0);
                // Send to destination
				Mover.moveAircraft(pirate, destination, game);
            }
        }
    }

    /**
     * Gives orders to my drones
     *
     * @param game - the current game state
     */
    private void handleDrones(PirateGame game) {
        // Go over all of my drones
        for (Drone drone : game.getMyLivingDrones()) {
            // Choose a destination
            City destination = game.getMyCities().get(0);
            // Send to destination game)
			Mover.moveAircraft(drone, destination, game);
        }
    }
}

