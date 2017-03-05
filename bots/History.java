package bots;

import pirates.*;

import java.util.*;

class History {

    static ArrayList<List<Pirate>> enemyPirates = new ArrayList<>();

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
        List<Location> thisTurnEnemyPiratesLocations = enemysPiratesLocation.get(enemysPiratesLocation.size() - 1);
        List<Integer> thisTurnEnemyPiratesHealth = enemysPiratesHealth.get(enemysPiratesLocation.size() - 1);
        for (Pirate pirate : game.getEnemyLivingPirates()) {
            thisTurnEnemyPiratesLocations.add(new Location(pirate.location.row, pirate.location.col));//create copy
            thisTurnEnemyPiratesHealth.add(pirate.currentHealth);


        }
        enemyPirates.add(game.getAllEnemyPirates());

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


    static boolean isEnemyPirateStateNothing(PirateGame game, Pirate pirate) {
        int id = getPirateId(game, pirate);
        for (List<Pirate> enemyPirate : enemyPirates) {
            if (enemyPirate.get(id).getLocation().compareTo(enemyPirate.get(id).initialLocation) != 0) {
                return false;
            }
        }
        game.debug("true");
        return true;

    }


    static int getPirateId(PirateGame game, Pirate pirate) {
        int id = 0;
        for (List<Pirate> pirateList : enemyPirates) {
            for (Pirate p : pirateList) {
                if (p.id == pirate.id) {
                    id = p.id;
                }
            }
            return id;
        }
        return id;
    }
}
