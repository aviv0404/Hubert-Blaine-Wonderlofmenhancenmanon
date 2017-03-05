package bots;
import pirates.*;
import java.util.List;

/**
 * Agada bot :)
 */
public class MyBot implements PirateBot {
    private History history;
    private Strategy strategy;

    @Override
    public void doTurn(PirateGame game) {
        
        if(firstTurn(game)) initFirstTurn();
        history.update(game.getMyLivingPirates(),game);
        if(game.getTurn() == 1){
            strategy = decideStrategy(game);
        }
        strategy.doTurn(game,history);
        
    }

    private boolean firstTurn(PirateGame game) {
        return game.getTurn() == 1;
    }

    private void initFirstTurn() {
        history = new History();
    }

    private Strategy decideStrategy(PirateGame game) {
        if (game.getEnemyCities().size() == 0 && game.getMyCities().size() != 0){
            return new AttackStrategy();
        }
        if (game.getMyCities().size() == 0 && game.getEnemyCities().size() != 0){
            return new DefendStrategy();
        }
        if (game.getMyCities().size() > 0 && game.getEnemyCities().size() > 0){
            return new SpecialStrategy(history);
        }
        if (game.getEnemyCities().size() == 0 && game.getMyCities().size() == 0){
            return new SpecialStrategy(history);
        }
        return null;
    }
}
