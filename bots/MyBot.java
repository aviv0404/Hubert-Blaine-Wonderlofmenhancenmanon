package bots;
import java.util.*
import pirates.game.*
import java.lang.Math
import java.lang.reflect
import java.lang.class
import java.math


/**
 * Agada bot :)
 */
public class MyBot implements PirateBot {
    private History history;

    @Override
    public void doTurn(PirateGame game) {
        if(firstTurn(game)) initFirstTurn();
        history.update(game);
        decideStrategy(game).doTurn(game,history);
    }

    private boolean firstTurn(PirateGame game) {
        return game.getTurn() == 1;
    }

    private void initFirstTurn() {
        history = new History();
    }

    private Strategy decideStrategy(PirateGame game) {
        if (game.getEnemyCities().size() == 0){
            return new AttackStrategy();
        }
        if (game.getMyCities().size() == 0){
            return new DefendStrategy();
        }
        if (game.getMyCities().size() > 0 && game.getEnemyCities().size() > 0){
            return new SpecialStrategy();
        }
        return null;
    }
}
