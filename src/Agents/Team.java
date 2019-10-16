package Agents;

import java.util.ArrayList;

public class Team {
    boolean hasPosession=false;
    int difScore = 0;
    String tactics = "";
    ArrayList<Player> Players;

    public Team(ArrayList<Player> players) {
        Players = players;
    }

    public boolean isHasPosession() {
        return hasPosession;
    }

    public void setHasPosession(boolean hasPosession) {
        this.hasPosession = hasPosession;
    }

    public int getDifScore() {
        return difScore;
    }

    public void setDifScore(int difScore) {
        this.difScore = difScore;
    }

    public String getTactics() {
        return tactics;
    }

    public void setTactics(String tactics) {
        this.tactics = tactics;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

}
