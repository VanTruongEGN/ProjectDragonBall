package AI;

import module.Player;

import java.util.ArrayList;

public class GameState {
    Player goku;
    public Player vegeta;
    ArrayList<GameState> listState;
    int skillIndex;
    public GameState(Player goku, Player vegeta,  int skillIndex) {
        this.listState = new ArrayList<>();
        this.goku = goku;
        this.vegeta = vegeta;
        this.skillIndex = skillIndex;
    }
    public GameState(Player goku, Player vegeta) {
        this.listState = new ArrayList<>();
        this.goku = goku;
        this.vegeta = vegeta;
    }

    public void addNewState(GameState state) {
        listState.add(state);
    }
}
