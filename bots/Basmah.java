package bots;

import pirates.*;
import java.util.*;

class Basmah implements Strategy{
    private PirateGame game;
    private Player player;
    public void doTurn(PirateGame game, History history){
        this.game = game;
        player = game.getMyself();
        game.debug("Activated: Basmah");
        
    }
}