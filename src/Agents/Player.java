package Agents;

import Utils.Position;

public class Player {
    char type;
    Position position= new Position(0,0);
    boolean hasBall=false;
    //int numberSteps=0; se nao for para ter drible
    //int timeWithBall=0; se for para ter um maximo de tempo com bola (5 seg por exemplo)
    int number;
    int closePercentage=70;
    int triplePercentage=40;
    int passingTendency=60;

    public Player(char type, int number) {
        this.type = type;
        this.number = number;
        switch (type)
        {
            case 'g': // Jogador guloso
            {
                passingTendency=10;
                closePercentage=75;
                triplePercentage=60;
            }
            case 's': // Bom lançador
            {
                closePercentage=45;
                triplePercentage=65;
            }
            case 'f': // Bom finisher
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

}
