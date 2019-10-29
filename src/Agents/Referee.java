package Agents;

import jade.core.Agent;
import jade.core.behaviours.*;
import Agents.Player.joiGame;
import Utils.Position;
import Utils.Stats;
import jade.core.AID;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAException;

public class Referee extends Agent {
    public final static String JOINREF = "JOINREF";
    public final static String JOINNED = "JOINNED";
    public final static String FAILED = "FAILED";

	private int timer = 60*4;


	protected void setup()
    {
		addBehaviour( new joiGame(this) );
    }

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*************************************************************/
	class joiGame extends SimpleBehaviour {
		private Referee father;
		private AID agentGame = null;
		private boolean joinned = false;
	    
	    public joiGame( Agent a ) {
	    	super(a);
	    	this.father = (Referee) a;
	    }
	    
		public void action() 
        {
			
			// Search Game
			while(this.agentGame == null) {

	        	System.out.println("searching game");
	        	
	        	this.agentGame = getService("game");
				
				ACLMessage m = new ACLMessage( ACLMessage.INFORM );
		        m.setContent( JOINREF );
		        m.addReceiver( this.agentGame );
	
		        send( m );
		        
		        try {
	                Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
	        while(!this.joinned) {
	        	
	        	try {
	                Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	System.out.println("running referee");
	        	
	        	ACLMessage msg = receive();

	            if (msg != null) {
	            	System.out.println("referee caught msg!");
	            	AID sender = msg.getSender();
	            	 if (JOINNED.equals( msg.getContent() )) {
	         			System.out.println("Referee joined game!");  
	         			this.joinned = true;
	            	 } else if (FAILED.equals( msg.getContent() )) {
	         			System.out.println("Error couldn't join a game!");
	            	 } else {
	          			System.out.println("Got the wrong message?!?");            		 
	            	 }
	            }
	        }
        	this.finished = true;
        }
		
        private boolean finished = false;
        public  boolean done() {  return finished;  }
	}
	
	/*************************************************************/
	/*                     DF managing                           */
	/*************************************************************/
	void register( ServiceDescription sd)
//  ---------------------------------
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }
	
	protected void takeDown() 
//  ---------------------------------
    {
       try { DFService.deregister(this); }
       catch (Exception e) {}
    }
	
	AID getService( String service )
//  ---------------------------------
	{
		DFAgentDescription dfd = new DFAgentDescription();
   		ServiceDescription sd = new ServiceDescription();
   		sd.setType( service );
		dfd.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(this, dfd);
			if (result.length>0)
				return result[0].getName() ;
		}
        catch (FIPAException fe) { fe.printStackTrace(); }
      	return null;
	}
}