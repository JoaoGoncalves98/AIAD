package Agents;

import jade.core.Agent;

import jade.core.behaviours.*;
import Agents.Game.gatherPlayers;
import Utils.*;
import jade.core.AID;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Player extends Agent {

	private Utils utils = new Utils( this );

	private Position position= new Position(0,0);
    private boolean hasBall=false;
    private int team = 0;
    private Stats stats = null;

	protected void setup()
    {
		
		Object[] args = getArguments();
		if (args != null && args.length == 1) {
        	int stats = -1;
            stats = (int) args[0];
        	this.stats = new Stats(stats);  
			
		} else {
        	System.out.println("Wrong arguments - will be a normal player (standard stats)");
        }

		// does first behaviour!
		addBehaviour( new joiGame(this) );

		// does second behaviour!
		addBehaviour( new playingGame(this) );
        
    }

	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*************************************************************/
	class joiGame extends SimpleBehaviour {
		private Player father;
		private AID agentGame = null;
		private boolean joinned = false;

		public joiGame( Agent a ) {
			super(a);
			this.father = (Player) a;
		}

		public void action()
		{

			// Search Game
			while(this.agentGame == null) {

				System.out.println("searching game");

				this.agentGame = this.father.utils.getService("game");

				ACLMessage m = new ACLMessage( ACLMessage.INFORM );
				m.setContent( this.father.utils.JOIN );
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
				System.out.println("running player");

				ACLMessage msg = receive();

				if (msg != null) {
					System.out.println("player caught msg!");
					AID sender = msg.getSender();
					if (this.father.utils.JOINNED.equals( msg.getContent() )) {
						System.out.println("Joined game!");
						this.joinned = true;
					} else if (this.father.utils.FAILED.equals( msg.getContent() )) {
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
	/*************************************************************/
	class playingGame extends SimpleBehaviour {
		private Player father;
		private AID agentGame = null;
		private boolean joinned = false;

		public playingGame( Agent a ) {
			super(a);
			this.father = (Player) a;
		}

		public void action()
		{
			// Check if Game started
			while(this.agentGame == null) {

				System.out.println("game started?");

				this.agentGame = this.father.utils.getService("gamestarted");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			while(true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("running player");

			}
			// this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}

	/************************************************************/
	/*                     Gets and Sets                         */
	/*************************************************************/
	public Stats getStats() {
		return stats;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean hasBall() {
		return hasBall;
	}

	public void setHasBall(boolean hasBall) {
		this.hasBall = hasBall;
	}

	public int basketRange(){
		double getNetDist;
		if(team==1) //Aqui o cesto vai tar na posição x=15, y=5
			getNetDist = Math.sqrt(Math.pow(Math.abs(15-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
		else //Aqui o cesto vai tar na posição x=0, y=5
			getNetDist = Math.sqrt(Math.pow(Math.abs(0-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
		if (getNetDist<4) // close range
			return 0;
		if (getNetDist <6) // short 3-pointer
			return 1;
		else // long shot
			return 2;
	}

	public boolean isMidRange(){
		double getNetDist;
		if(team==1) //Aqui o cesto vai tar na posição x=15, y=5
			getNetDist = Math.sqrt(Math.pow(Math.abs(15-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
		else //Aqui o cesto vai tar na posição x=0, y=5
			getNetDist = Math.sqrt(Math.pow(Math.abs(0-position.getX()),2) + Math.pow(Math.abs(5-position.getY()),2));
		return (getNetDist<4);
	}

}
//Get stats has args
		/*
		Object[] args = getArguments();
		if (args != null && args.length == 5) {
     	int[] stats = {0, 0, 0, 0, 0};
     	for (int i = 0; i<args.length; i++) {
             stats[i] = (int) args[i];
         }
     	this.stats = new Stats(stats[0], stats[1], stats[2], stats[3], stats[4]);        
     } else {
     	System.out.println("Wrong arguments");
     	return;
     }
     */
