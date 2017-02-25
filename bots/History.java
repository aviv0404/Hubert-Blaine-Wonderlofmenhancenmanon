package bots;
import java.util.ArrayList;
import java.util.List;
import pirates.*;

class History {
    private List<List<Location>> enemysPiratesLocation;
    private List<List<Integer>> enemysPiratesHealth;
    public History(PirateGame game)
    {
        enemysPiratesLocation = new ArrayList<>();
        enemysPiratesHealth = new ArrayList<>();
    }
    void update(PirateGame game) 
    {
        enemysPiratesLocation.add(new ArrayList<>());
        enemysPiratesHealth.add(new ArrayList<>());
        List<Location> thisTurnEnemyPiratesLocations = enemysPiratesLocation.get(enemysPiratesLocation.size()-1);
        List<Integer> thisTurnEnemyPiratesHealth = enemysPiratesHealth.get(enemysPiratesLocation.size()-1);
        for (Pirate pirate : game.getEnemyLivingPirates()) {
            thisTurnEnemyPiratesLocations.add(new Location(pirate.location.row,pirate.location.col));//create copy
            thisTurnEnemyPiratesHealth.add(pirate.currentHealth);
            
        }
    }
    public List<Location> getEnemyPiratesLocatin(int turn)//return copy
    {
        List<Location> copy = new ArrayList<>();
        for (Location l : enemysPiratesLocation.get(turn)) {
            copy.add(l);
        }
        return copy;
    }
    public List<Integer> getEnemyPiratesHealth(int turn)//return copy
    {
        List<Integer> copy = new ArrayList<>();
        for (Integer i : enemysPiratesHealth.get(turn)) {
            copy.add(i);
        }
        return copy;
    }
}
