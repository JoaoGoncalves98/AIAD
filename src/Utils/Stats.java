package Utils;

public class Stats {
    private int insideScorePercentage = 50;
	private int outsideScorePercentage = 50;
	private int passPercentage = 50;
	private int type = 0;
    
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
		type = i;
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
		default: // Normal
			break;
    	}
    }

	public int gettype() {
		return this.type;
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
}
