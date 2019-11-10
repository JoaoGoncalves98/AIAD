package Utils;

public class BasketballCourt {
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
        for (int i = 0 ; i < this.court.length ; i++)
            for (int j = 0 ; j < this.court[0].length ; j++)
                this.court[i][j] = "  ";
        int inc = 0;
        for (int i = 0 ; i < this.court.length-1 && inc < this.nPlayers/2 ; i++)
            for (int j = 0 ; j < this.court[0].length-1 && inc < this.nPlayers/2 ; j++)
                if( (i == 0 && (j == this.court[0].length *2 / 4 || j == this.court[0].length *3 / 4)) ||
                        (i == 2 && (j == this.court[0].length *2 / 5 || j == this.court[0].length *3 / 5 || j == this.court[0].length *4 / 5))) {
                    inc++;
                    this.court[i][j] = this.team1C + Integer.toString(inc);
                }
        for (int i = this.court.length-1 ; i >= 0 && inc > 0 ; i--)
            for (int j = 0 ; j < this.court[0].length-1 && inc > 0 ; j++)
                if( (i == this.court.length-1 && (j == this.court[0].length *2 / 4 || j == this.court[0].length *3 / 4)) ||
                    (i == this.court.length-3 && (j == this.court[0].length *2 / 5 || j == this.court[0].length *3 / 5 || j == this.court[0].length *4 / 5))) {
                    this.court[i][j] = this.team2C + Integer.toString(inc);
                    inc--;
                }
    }

    public void printCourt() {
        for (int j = 0 ; j < this.court[0].length ; j++) {
            System.out.print("|   ");
            for (int i = 0 ; i < this.court.length ; i++) {
                System.out.print(this.court[i][j] + "   ");
            }
            System.out.println("|");
        }
    }
}
