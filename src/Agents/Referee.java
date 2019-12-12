package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Referee extends Agent {

	private Utils utils = new Utils( this );

	private AID agentGame = null;

	private int timer = 20;
	//private int timer = 30;

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


				this.agentGame = this.father.utils.getService("game");

				ACLMessage m = new ACLMessage( ACLMessage.INFORM );
				m.setContent(Utils.JOINREF);
				m.addReceiver( this.agentGame );
				this.father.setGame( this.agentGame );

				send( m );

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			while(!this.joinned) {

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ACLMessage msg = receive();

				if (msg != null) {
					AID sender = msg.getSender();
					if (Utils.JOINNED.equals( msg.getContent() )) {
						System.out.println("Referee joined game!");
						this.joinned = true;
					} else if (Utils.FAILED.equals( msg.getContent() )) {
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
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ACLMessage msg = receive();

				if (msg != null) {
					AID sender = msg.getSender();
					if (Utils.STARTGAME.equals( msg.getContent() )) {
						System.out.println("Referee will start the clock!");
						this.flag = !this.flag;

						ACLMessage m = new ACLMessage( ACLMessage.INFORM );
						m.setContent(Utils.STARTEDGAME);
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
				Thread.sleep(this.father.timer * 1000); //TEMPO DE JOGO SEM TIMERS FICA A QUANTO HMMMM?
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Referee stopped the clock! Ending GAME!");

			ACLMessage m = new ACLMessage( ACLMessage.INFORM );
			m.setContent(Utils.ENDEDGAME);
			m.addReceiver( this.father.agentGame );

			send( m );

			this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}
}