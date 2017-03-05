package bots;
import pirates.*;

class Attacker {
	/**
    * Makes the pirate try to attack. Returns true if it did.
    *
    * @param pirate - the attacking pirate
    * @param game - the current game state
    * @return - true if the pirate attacked
    */
    static boolean tryAttack(Pirate pirate, PirateGame game) {
        // Go over all enemies
        for (Aircraft enemy : game.getEnemyLivingAircrafts()) {
            // Check if the enemy is in attack range
            if (pirate.inAttackRange(enemy)) {
                // Fire!
                game.attack(pirate, enemy);
                // Print a message
                //game.debug("pirate " + pirate + " attacks " + enemy);
                // Did attack
                return true;
            }
        }
        // Didn't attack
        return false;
    }
    
    static boolean tryAttackDrones(Pirate pirate,PirateGame game){
        if (!game.getEnemyLivingDrones().isEmpty()){
            for (Drone drone : game.getEnemyLivingDrones()){
                if (pirate.inAttackRange(drone)){
                    game.attack(pirate,drone);
                    return true;
                }
            }
        }
        return false;
    }
}
