package Agents;

import Utils.Position;

public class Player {
    private char type;
    private Position position= new Position(0,0);
    private boolean hasBall=false;
    private int teamNumber;
    //private int numberSteps=0; se nao for para ter drible
    //private int timeWithBall=0; se for para ter um maximo de tempo com bola (5 seg por exemplo)
    private int number;
    private int closePercentage=70;
    private int triplePercentage=40;
    private int passingTendency=60;

    public Player(char type, int number, int teamNumber) {
        this.type = type;
        this.number = number;
        this.teamNumber = teamNumber;
        switch (type)
        {
            case 'g': // Greedy Player
            {
                passingTendency=10;
                closePercentage=75;
                triplePercentage=60;
            }
            case 's': // Good shooter
            {
                closePercentage=45;
                triplePercentage=65;
            }
            case 'f': // Good finisher
            {
                closePercentage=85;
                triplePercentage=20;
            }
            default:
        }

    }

    public char getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasBall() {
        return hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    public int getNumber() {
        return number;
    }

    public int getClosePercentage() {
        return closePercentage;
    }

    public int getTriplePercentage() {
        return triplePercentage;
    }

    public boolean isCloseRange(){
        double getNetDist;
        if(teamNumber==1) //Aqui o cesto vai tar na posição x=15, y=5
            getNetDist = Math.sqrt(Math.pow(Math.abs(15-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
        else //Aqui o cesto vai tar na posição x=0, y=5
            getNetDist = Math.sqrt(Math.pow(Math.abs(0-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
        return (getNetDist<4);
    }

    //Falta fazer o move player que tb tem de saber a posição de todos os jogadores e (0<= x <=15) (0<= y <=10)

}
