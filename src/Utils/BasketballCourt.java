package Utils;

import java.io.Serializable;
import java.util.Random;
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
                if(s.toUpperCase().equals(this.court[i][j]))
                    return true;
        return false;
    }

    public boolean openSpaceInDir( String s, String dir)
    {
        for(int i = 0 ; i < this.court.length ; i++)
            for(int j = 0 ; j < this.court[0].length ; j++ )
                if(s.toUpperCase().equals(this.court[i][j]) || s.toLowerCase().equals(this.court[i][j]))
                {
                    switch(dir)
                    {
                        case "U":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i+1,j)) {
                                    if (this.court[i + 1][j].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i-1,j)) {
                                    if (this.court[i - 1][j].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "D":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i-1,j)) {
                                    if (this.court[i - 1][j].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i+1,j)) {
                                    if (this.court[i + 1][j].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "L":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i,j-1)) {
                                    if (this.court[i][j - 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i,j+1)) {
                                    if (this.court[i][j + 1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "R":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i,j+1)) {
                                    if (this.court[i][j + 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i,j-1)) {
                                    if(this.court[i][j-1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "UL":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i+1,j-1)) {
                                    if (this.court[i + 1][j - 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i-1,j+1)) {
                                    if (this.court[i - 1][j + 1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "UR":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i+1,j+1)) {
                                    if (this.court[i + 1][j + 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i-1,j-1)) {
                                    if (this.court[i - 1][j - 1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "DL":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i-1,j-1)) {
                                    if (this.court[i - 1][j - 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i+1,j+1)) {
                                    if (this.court[i + 1][j + 1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        case "DR":
                            if(s.toLowerCase().contains("a"))
                            {
                                if(confirmRange(i-1,j+1)) {
                                    if (this.court[i - 1][j + 1].toLowerCase().contains("b"))
                                        return false;
                                    else return true;
                                }
                            }
                            else {
                                if(confirmRange(i+1,j-1)) {
                                    if (this.court[i + 1][j - 1].toLowerCase().contains("a"))
                                        return false;
                                    else return true;
                                }
                            }
                        default: return false;
                    }
                }
        return false;
    }

    public boolean openSpace( String s, int lim ) {
        for(int i = 0 ; i < this.court.length ; i++)
            for(int j = 0 ; j < this.court[0].length ; j++ )
                if(s.toUpperCase().equals(this.court[i][j]) || s.toLowerCase().equals(this.court[i][j])) {
                    if(s.toLowerCase().contains("a")) {
                        for(int ii = -lim ; ii < lim ; ii++)
                            for(int jj = -lim ; jj < lim ; jj++ )
                                if(confirmRange(i+ii,j+jj))
                                if(!(ii == 0 && jj == 0))
                                    if(this.court[i+ii][j+jj].toLowerCase().contains("b"))
                                        return false;
                    } else {
                        for(int ii = -lim ; ii < lim ; ii++)
                            for(int jj = -lim ; jj < lim ; jj++ )
                                if(confirmRange(i+ii,j+jj))
                                if(!(ii == 0 && jj == 0))
                                    if(this.court[i+ii][j+jj].toLowerCase().contains("a"))
                                        return false;
                    }
                    return true;
                }
        return true;
    }

    public int[] getPos( String s ) {
        int[] a = {0,0};
        for(int i = 0 ; i < this.court.length ; i++)
            for(int j = 0 ; j < this.court[0].length ; j++ )
                if(this.court[i][j].toLowerCase().equals(s)) {
                    a[0]=i;
                    a[1]=j;
                    return a;
                }
        return a;
    }

    public String[][] getCourt() {
        return  this.court;
    }

    public boolean confirmRange(int i, int j) {
        if(i >= 0 && i < this.court.length && j >= 0 && j < this.court[0].length)
            return true;
        return false;
    }

    public boolean updatePos( String s, int ii, int jj) {
        if(this.confirmRange(ii, jj) && this.court[ii][jj].equals("  ")) {
            System.out.println("Entrou1");
            for(int i = 0 ; i < this.court.length ; i++)
                for(int j = 0 ; j < this.court[0].length ; j++ )
                    if(this.court[i][j].equals(s.toUpperCase())) {
                        System.out.println("Entrou2");
                        this.court[i][j] = "  ";
                        this.court[ii][jj] = s.toUpperCase();
                        return true;
                    } else if(this.court[i][j].equals(s)) {
                        System.out.println("Entrou2");
                        this.court[i][j] = "  ";
                        this.court[ii][jj] = s;
                        return true;
                    }
        }
        return false;
    }

    public boolean passingLaneOpen(String s, String p)
    {
        return false;
    }

    public boolean makePass( String passer, String toPass, int passStat ) {
        int[] posPasser = this.getPos( passer );
        int[] posToPass = this.getPos( toPass );
        int[] diff = {((posPasser[0]-posToPass[0])/Math.abs(posPasser[0]-posToPass[0])), ((posPasser[1]-posToPass[1])/Math.abs(posPasser[1]-posToPass[1]))};
        int[] posIntercepter = {0,0};
        int intercepter = 0;
        String intercepterName = "";

        // See if there is a intercepter
        for(int i = posPasser[0] ; i != posToPass[0] ; i = i + diff[0])
            for(int j = posPasser[1] ; j != posToPass[1] ; j = j + diff[1])
                if(!(this.court[i][j].contains(""+passer.toLowerCase().charAt(0)) || this.court[i][j].equals("  "))) {
                    intercepter++;
                    intercepterName = this.court[i][j];
                    posIntercepter[0] = i;
                    posIntercepter[1] = j;
                }

        // calculate probability
        int prob = 20;
        prob += (int) (passStat*20)/100;
        if(intercepter>0) {
            //  probability will be the distance from the center times 80;
            int[] center = {(Math.abs(posPasser[0])+Math.abs( posToPass[0] ))/2,(Math.abs(posPasser[1])+Math.abs( posToPass[1] ))/2};
            double dist = Math.sqrt(Math.pow(center[0]-posIntercepter[0],2)+Math.pow(center[1]-posIntercepter[1],2));
            double maxdist = Math.sqrt(Math.pow(center[0]-posPasser[0],2)+Math.pow(center[1]-posPasser[1],2));
            prob += (int) ((maxdist-dist)*60)/maxdist;
        } else {
            prob += 60;
        }

        // Now make pass
        Random r = new Random();
        int p = r.nextInt(100 + 1);

        if(p<=prob) {
            // pass success
            this.court[posPasser[0]][posPasser[1]] = this.court[posPasser[0]][posPasser[1]].toLowerCase();
            this.court[posToPass[0]][posToPass[1]] = this.court[posToPass[0]][posToPass[1]].toUpperCase();
            return true;
        } else {
            // pass failure
            this.court[posIntercepter[0]][posIntercepter[1]] = this.court[posIntercepter[0]][posIntercepter[1]].toUpperCase();
            this.court[posPasser[0]][posPasser[1]] = this.court[posPasser[0]][posPasser[1]].toLowerCase();
            return false;
        }
    }
}
