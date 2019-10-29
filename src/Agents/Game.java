package Agents;

import jade.core.Agent;
import jade.core.behaviours.*;

import Utils.*;
import jade.core.AID;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Game extends Agent {
    public final static String JOIN = "JOIN";
    public final static String JOINNED = "JOINNED";
    public final static String FAILED = "FAILED";
    public final static String JOINREF = "JOINREF";
    
    private int nPlayers = 4;
	private Team team1 = new Team(this.nPlayers/2, "white");
	private Team team2 = new Team(this.nPlayers/2, "black");
	private AID referee = null;	
	
	protected void setup() {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "game" );
        sd.setName( getLocalName() );
        this.register( sd );
        
        addBehaviour( new gatherPlayers(this, this.nPlayers) );
    }

	public Team getTeam1() { return this.team1; };
	public Team getTeam2() { return this.team2; };
	public void setReferee(AID r) { this.referee = r; };

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*************************************************************/
	class gatherPlayers extends SimpleBehaviour {
		private int incPlayers = 0;
		private int incReferee = 0;
		private int nPlayers = 0;
		private Game father;
	    
	    public gatherPlayers( Agent a, int limit) {
	    	super(a);
	    	this.father = (Game) a;
	    	this.nPlayers = limit;
	    }
	    
		public void action() 
        {
			while(this.incPlayers != this.nPlayers || this.incReferee != 1) {
	            ACLMessage msg = receive();

	            try {
	               Thread.sleep(2000);
	            }
	            catch (Exception e) {}
	            
	        	System.out.println("running game");
	        	
	            if (msg != null) {
	            	System.out.println("game caught msg!");
	            	AID sender = msg.getSender();
	            	System.out.println(sender);
	                if (JOIN.equals( msg.getContent() )) {
	                	if(this.father.getTeam1().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent( JOINNED );
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
	                	}
	            		else if(this.father.getTeam2().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent( FAILED );
	                        m.addReceiver( sender );
	
	                        send( m );      
	                        
	                        this.incPlayers++;          			
	            		}
	                } else if (JOINREF.equals( msg.getContent() )) {
	                	this.father.setReferee(sender);
	                	
	                	ACLMessage m = new ACLMessage( ACLMessage.INFORM );
                        m.setContent( JOINNED );
                        m.addReceiver( sender );

                        send( m );
                        
	                	this.incReferee++;
                        
	                } else {
            			System.out.println("This message wasn't suposed to come here xd");
            		}
	            } else {
	                // if no message is arrived, block the behaviour
	                block();
	            }
			}
        	System.out.println("All players were gathered");
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
}
