package Agents;


import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.*;

public class Game extends Agent {

	private Utils utils = new Utils( this );
    
    private int nPlayers = 4;
	private Team team1 = new Team(this.nPlayers/2, "white");
	private Team team2 = new Team(this.nPlayers/2, "black");
	private AID referee = null;	
	
	protected void setup() {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "game" );
        sd.setName( getLocalName() );
        this.utils.register( sd );

        /* Does first behaviour */
		addBehaviour( new gatherPlayers(this, this.nPlayers) );

		/* Does second behaviour */
		addBehaviour( new warnReferee(this) );

		/* Does third behaviour */
		addBehaviour( new regulatingGame(this) );

	}

	public Team getTeam1() { return this.team1; };
	public Team getTeam2() { return this.team2; };
	public void setReferee(AID r) { this.referee = r; };

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*					 Gather Players							 */
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
	                if (this.father.utils.JOIN.equals( msg.getContent() )) {
	                	if(this.father.getTeam1().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent( this.father.utils.JOINNED );
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
	                	}
	            		else if(this.father.getTeam2().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent( this.father.utils.FAILED );
	                        m.addReceiver( sender );
	
	                        send( m );      
	                        
	                        this.incPlayers++;          			
	            		}
	                } else if (this.father.utils.JOINREF.equals( msg.getContent() )) {
	                	this.father.setReferee(sender);
	                	
	                	ACLMessage m = new ACLMessage( ACLMessage.INFORM );
                        m.setContent( this.father.utils.JOINNED );
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
        public  boolean done() {
			this.father.takeDown(); // Deletes DF entry
			return finished;
        }
	}

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*					 Warn Referee							 */
	/*************************************************************/

	class warnReferee extends SimpleBehaviour {
		private Game father;
		private boolean flag = true;

		public warnReferee( Agent a) {
			super(a);
			this.father = (Game) a;
		}

		public void action() {
			ACLMessage m = new ACLMessage( ACLMessage.INFORM );
			m.setContent( this.father.utils.STARTGAME );
			m.addReceiver( this.father.referee );

			send( m );

			while(this.flag) {

				System.out.println("warning ref - everything ready!");

				try {
					Thread.sleep(2000);
				}
				catch (Exception e) {}

				ACLMessage msg = receive();

				if (msg != null) {
					System.out.println("game caught msg!");
					AID sender = msg.getSender();
					System.out.println(sender);
					if (this.father.utils.STARTEDGAME.equals( msg.getContent() )) {
						this.flag = !this.flag;
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
			System.out.println("READY FOR CALCULATIONS!");
			this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*					 Regulating Game						 */
	/*************************************************************/

	class regulatingGame extends SimpleBehaviour {
		private Game father;

		public regulatingGame( Agent a ) {
			super(a);
			this.father = (Game) a;
		}

		public void action() {

			// Função para criar game matrix

			ServiceDescription sd  = new ServiceDescription();
			sd.setType( "gamestarted" );
			sd.setName( getLocalName() );
			// sd.addProperties( string ou matriz do jogo! );
			// ver properties em ../Jade_Primer/primer5.html#utilities

			this.father.utils.register( sd );

			while(true) {
				try {
					Thread.sleep(3000);
				}
				catch (Exception e) {}
				System.out.println("READY FOR CALCULATIONS!");
				// TODO: 08/11/2019
			}

			// this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}
}
