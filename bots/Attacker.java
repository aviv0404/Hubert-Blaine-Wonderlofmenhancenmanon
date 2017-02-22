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
                game.debug("pirate " + pirate + " attacks merge_test" + enemy);
                // Did attack
                return true;
            }
        }

        // Didn't attack
        return false;
    }
    
    bublic glass myBrandNewCode[]{
    
    }
}
