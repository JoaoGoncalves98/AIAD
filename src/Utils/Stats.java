package Utils;

public class Stats {
    private int scorePercentage = 50;
    private int passPercentage = 50;
    private int dribblePercentage = 50;
    private int stealPercentage = 50;
    private int interceptPercentage = 50;
    
/*	
  	public Stats(int score, int pass, int dribble, int steal, int intercept) {
		scorePercentage = score;
	    passPercentage = pass;
	    dribblePercentage = dribble;
	    stealPercentage = steal;
	    interceptPercentage = intercept; 
	}
*/
    public Stats(int i) {
    	switch(i) {
    	case 0: // Scorer
    		this.scorePercentage = 80; 		
    	    break;
    	case 1: // Passer
    		this.passPercentage = 80;
    	    break;
    	case 2: // Dribbler
    		this.dribblePercentage = 80;
    		break;
    	case 3: // Stealer
    		this.stealPercentage = 80;
    		break;
    	case 4: // Intercepter
    		this.interceptPercentage = 80;
    		break;
		default: // Normal
			break;
    	}
    }
    
    public int getscorePercentage() {
		return this.scorePercentage;
	}
	public int getpassPercentage() {
		return this.passPercentage;
	}
	public int getdribblePercentage() {
		return this.dribblePercentage;
	}
	public int getstealPercentage() {
		return this.stealPercentage;
	}
	public int getinterceptPercentage() {
		return this.interceptPercentage;
	}
}
