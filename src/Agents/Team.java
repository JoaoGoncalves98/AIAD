package Agents;

import java.util.ArrayList;

public class Team {
    private boolean hasPosession=false;
    private int difScore = 0;
    private String tactics = "";
    private ArrayList<Player> players;
    private Manager manager = new Manager();

    public Team(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean hasPosession() {
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




    public ArrayList<Player> getPlayers() {
        return players;
    }


    public int passBall() {
        if (hasPosession) {
            for (Player player : players) {
                //Podia fazer aqui uma %de acerto de passe de 85% e tb mudar se houver alguem pelo caminho
                if(!player.hasBall())
                {
                    player.setHasBall(true);
                    Ball.position=player.getPosition();
                }
                else
                {
                    player.setHasBall(false);
                }
            }

            //then change the ball position to the player who has the ball
            return 0;
        }
        return -1;
    }

    public int shootBall() {
        if (hasPosession) {
            for (Player player : players) {
                if(player.hasBall())
                {
                    if(player.isCloseRange() && Math.random() <= player.getClosePercentage()) {
                        difScore += 2;
                        return 2;
                    }
                    if(!(player.isCloseRange()) && Math.random() <= player.getTriplePercentage()) {
                        difScore += 3;
                        return 3;
                    }

                    //then change posession to other team player and the ball position to that player
                    player.setHasBall(false);
                    hasPosession=false;
                    return 0;
                }
            }
        }
        return -1;
    }


    public int setTactics(String tactics) {
        return -1;
    }
}
