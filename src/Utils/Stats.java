package Utils;

public class Stats {
    private int insideScorePercentage = 50;
	private int outsideScorePercentage = 50;
    private int passPercentage = 50;
    private int dribblePercentage = 50;
    private int stealPercentage = 50;
    
/*	
  	public Stats(int score, int pass, int dribble, int steal, int intercept) {
		scorePercentage = score;
	    passPercentage = pass;
	    dribblePercentage = dribble;
	    stealPercentage = steal;
	    interceptPercentage = intercept; 
	}
*/
	public Stats()
	{
	}

    public Stats(int i) {
    	switch(i) {
    	case 0: // Inside Scorer
    		this.insideScorePercentage = 80;
    	    break;
    	case 1: // Inside Scorer
			this.outsideScorePercentage = 80;
			break;
    	case 2: // Passer
    		this.passPercentage = 80;
    	    break;
    	case 3: // Dribbler
    		this.dribblePercentage = 80;
    		break;
    	case 4: // Stealer
    		this.stealPercentage = 80;
    		break;
		default: // Normal
			break;
    	}
    }
    
    public int getInsideScorePercentage() {
		return this.insideScorePercentage;
	}
	public int getOutsideScorePercentage() {
		return this.outsideScorePercentage;
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
}
