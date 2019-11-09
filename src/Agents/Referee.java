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
	public final static String STARTGAME = "STARTGAME";
	public final static String STARTEDGAME = "STARTEDGAME";
	public final static String ENDEDGAME = "ENDEDGAME";

	private AID agentGame = null;

	private int timer = 60*4;

	protected void setup()
    {
		/* Does first behaviour */
		addBehaviour( new joiGame(this) );
		/* Does second behaviour */
		addBehaviour( new startGame(this) );
		/* Does second behaviour */
		addBehaviour( new startedGame(this ) );
    }

	public void setGame(AID g) { this.agentGame = g; };

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*                     Join Game                             */
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
				this.father.setGame( this.agentGame );

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
	/*                  Simple Behaviours                        */
	/*                     Start Game                            */
	/*************************************************************/
	class startGame extends SimpleBehaviour {
		private Referee father;
		private boolean flag = true;

		public startGame( Agent a ) {
			super(a);
			this.father = (Referee) a;
		}

		public void action()
		{
			while(this.flag) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("referee waiting!");

				ACLMessage msg = receive();

				if (msg != null) {
					System.out.println("referee caught msg!");
					AID sender = msg.getSender();
					if (STARTGAME.equals( msg.getContent() )) {
						System.out.println("Referee will start the clock!");
						this.flag = !this.flag;

						ACLMessage m = new ACLMessage( ACLMessage.INFORM );
						m.setContent( STARTEDGAME );
						m.addReceiver( this.father.agentGame );

						send( m );
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
			this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}
	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*                     Start Game                            */
	/*************************************************************/
	class startedGame extends SimpleBehaviour {
		private Referee father;

		public startedGame( Agent a ) {
			super(a);
			this.father = (Referee) a;
		}

		public void action()
		{
			System.out.println("Referee started the clock!");
			try {
				Thread.sleep(this.father.timer * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Referee stopped the clock! Ending GAME!");

			ACLMessage m = new ACLMessage( ACLMessage.INFORM );
			m.setContent( ENDEDGAME );
			m.addReceiver( this.father.agentGame );

			send( m );

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