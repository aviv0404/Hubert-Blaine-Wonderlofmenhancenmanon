package bots;
import pirates.*;

interface Strategy {
    public void doTurn(PirateGame game,History history);
}
