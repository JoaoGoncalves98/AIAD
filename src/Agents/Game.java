package Agents;

import java.util.ArrayList;

public class Game {
//    GameState: playerPosession; shooting; passing; noPosession

    private ArrayList<Team> teams;
    private int secondsLeft=180;

    public Game(ArrayList<Team> teams) {
        this.teams = teams;
    }

}
