package Utils;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class BasketballCourt implements Serializable {
    private int nPlayers = 0;
    private String court[][];
    private char team1C;
    private char team2C;

    public BasketballCourt( int nPlayers, char team1C, char team2C, int x, int y ) {
        this.nPlayers = nPlayers;
        this.court = new String[x][y];
        this.team1C = team1C;
        this.team2C = team2C;
    }

    public void initialize() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, this.nPlayers + 1);
        for (int i = 0 ; i < this.court.length ; i++)
            for (int j = 0 ; j < this.court[0].length ; j++)
                this.court[i][j] = "  ";
        int inc = 0;
        for (int i = 0 ; i < this.court.length-1 && inc < this.nPlayers/2 ; i++)
            for (int j = 0 ; j < this.court[0].length-1 && inc < this.nPlayers/2 ; j++)
                if( (i == 0 && (j == this.court[0].length / 3 || j == this.court[0].length *2 / 3)) ||
                        (i == 2 && (j == this.court[0].length / 4 || j == this.court[0].length *2 / 4 || j == this.court[0].length *3 / 4))) {
                    inc++;
                    if(inc == randomNum)
                        this.court[i][j] = this.team1C + Integer.toString(inc);
                    else
                        this.court[i][j] = Character.toLowerCase(this.team1C) + Integer.toString(inc);
                }
        for (int i = this.court.length-1 ; i >= 0 && inc > 0 ; i--)
            for (int j = 0 ; j < this.court[0].length-1 && inc > 0 ; j++)
                if( (i == this.court.length-1 && (j == this.court[0].length / 3 || j == this.court[0].length *2 / 3)) ||
                    (i == this.court.length-3 && (j == this.court[0].length / 4 || j == this.court[0].length *2 / 4 || j == this.court[0].length *3 / 4))) {
                    if(this.nPlayers/2 + inc == randomNum)
                        this.court[i][j] = this.team2C + Integer.toString(inc);
                    else
                        this.court[i][j] = Character.toLowerCase(this.team2C) + Integer.toString(inc);
                    inc--;
                }
    }

    public void printCourt() {
        System.out.print("j i");
        for (int i = 0 ; i < this.court.length ; i++)
            if (i < 10)
                System.out.print("  " + i + " ");
            else
                System.out.print("  " + i );

        System.out.println("  ");
        for (int j = 0 ; j < this.court[0].length ; j++) {
            if(j == 5)
                System.out.print(j + " O  ");
            else
                System.out.print(j + " |  ");
            for (int i = 0 ; i < this.court.length ; i++) {
                System.out.print(this.court[i][j] + "  ");
            }
            if(j == 5)
                System.out.println(j + "O");
            else
                System.out.println(j + "|");
        }
    }

    public boolean hasBall( String s ) {
        for(int i = 0 ; i < this.court.length ; i++)
            for(int j = 0 ; j < this.court[0].length ; j++ )
                if(s.toUpperCase() == this.court[i][j])
                    return true;
        return false;
    }
}
