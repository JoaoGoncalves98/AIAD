package Agents;

import jade.core.Agent;

import Utils.*;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Player extends Agent {

	private Utils utils = new Utils( this );

	private Position position= new Position(0,0);
    private boolean hasBall=false;
    private int team = 0;
    private Stats stats = new Stats();

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
	/*					   Join Game    						 */
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
				m.setContent(Utils.JOIN);
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
					if (Utils.JOINNED.equals( msg.getContent() )) {
						System.out.println("PLAYER JOINED GAME!");
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
	/*					  Playing Game    						 */
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
				// ESPERANDO Q O JOGO COMECE
				this.agentGame = this.father.utils.getService("gamestarted");
				System.out.println("game started?");
				try { Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace(); }
			}

			while(this.agentGame != null) {
				this.agentGame = this.father.utils.getService("gamestarted");
				ACLMessage msg = receive();
				if (msg != null) {
					AID sender = msg.getSender();
					if (Utils.PLAY.equals(msg.getContent())) {
						boolean f = true;
						while (f) {
							ACLMessage msg2 = receive();
							if (msg2 != null) {
								f = false;
								try {
									System.out.println("MSG that should be court content: " + msg2.getContentObject());
									BasketballCourt court = (BasketballCourt) msg2.getContentObject();

									if(court.hasBall(getLocalName()))
									{
										System.out.println("has ball");
										ACLMessage m = new ACLMessage( ACLMessage.INFORM );
										double random =  Math.random()*100;
										// passa a bola
										if(random <=20)
										{
											System.out.println(court.teammateName(getLocalName()) + " ");
											m.setContent(Utils.PASS + " " + court.teammateName(getLocalName()) + " " + stats.getpassPercentage()); //Ainda nao tem passing stat
											m.addReceiver( this.agentGame );

											send( m );
										}
									}

									else
										System.out.println("hasn't ball");


									if( getLocalName().contains("a") ) {
										System.out.println("enterd in a team agent");
										int pos[] = court.getPos(getLocalName());
										System.out.println(getLocalName() + " got position and it is equals to: " + pos[0] + " " + pos[1]);

										ACLMessage m = new ACLMessage( ACLMessage.INFORM );

										//random para saber qual é direção que o jogador quer mover
										double random =  Math.random()*100;
										System.out.println("random-number is:" + random);

										//Mover para a frente
										if (random <=25 && court.openSpaceInDir(getLocalName(),"U"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + pos[1]);

										//Mover para diagonal frente direita
										else if (random <=45 && court.openSpaceInDir(getLocalName(),"UR"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + (pos[1]+1));

										//Mover para diagonal frente esquerda
										else if (random <=65 && court.openSpaceInDir(getLocalName(),"UL"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + (pos[1]-1));

										//Mover para tras
										else if (random <=70 && court.openSpaceInDir(getLocalName(),"D"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + pos[1]);

										//Mover para diagonal tras direita
										else if (random <=75 && court.openSpaceInDir(getLocalName(),"DR"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + (pos[1]+1));

										//Mover para diagonal tras esquerda
										else if (random <=80 && court.openSpaceInDir(getLocalName(),"DL"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + (pos[1]-1));

										//Mover para a direita
										else if (random <=90 && court.openSpaceInDir(getLocalName(),"R"))
											m.setContent(Utils.RUN + " " + pos[0] + " " + (pos[1]+1));

										//Mover para a esquerda
										else if(court.openSpaceInDir(getLocalName(),"L"))
											m.setContent(Utils.RUN + " " + pos[0] + " " + (pos[1]-1));

										m.addReceiver( this.agentGame );

										send( m );
									}
									else {
										System.out.println("entered in b team agent");
										int[] pos = court.getPos(getLocalName());
										System.out.println(getLocalName() + " got position and it is equals to: " + pos[0] + " " + pos[1]);

										ACLMessage m = new ACLMessage( ACLMessage.INFORM );

										//random para saber qual é direção que o jogador quer mover
										double random =  Math.random()*100;
										System.out.println("random-number is:" + random);

										//Mover para a frente
										if (random <=25  && court.openSpaceInDir(getLocalName(),"U"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + pos[1]);

										//Mover para diagonal frente direita
										else if (random <=45 && court.openSpaceInDir(getLocalName(),"UR"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + (pos[1]-1));

										//Mover para diagonal frente esquerda
										else if (random <=65 && court.openSpaceInDir(getLocalName(),"UL"))
											m.setContent(Utils.RUN + " " + (pos[0]-1) + " " + (pos[1]+1));

										//Mover para tras
										else if (random <=70 && court.openSpaceInDir(getLocalName(),"D"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + pos[1]);

										//Mover para diagonal tras direita
										else if (random <=75 && court.openSpaceInDir(getLocalName(),"DR"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + (pos[1]-1));

										//Mover para diagonal tras esquerda
										else if (random <=80 && court.openSpaceInDir(getLocalName(),"DL"))
											m.setContent(Utils.RUN + " " + (pos[0]+1) + " " + (pos[1]+1));

										//Mover para a direita
										else if (random <=90 && court.openSpaceInDir(getLocalName(),"R"))
											m.setContent(Utils.RUN + " " + pos[0] + " " + (pos[1]-1));

										//Mover para a esquerda
										else if(court.openSpaceInDir(getLocalName(),"L"))
											m.setContent(Utils.RUN + " " + pos[0] + " " + (pos[1]+1));

										m.addReceiver( this.agentGame );

										send( m );
									}



								} catch (UnreadableException e) {
									e.printStackTrace();
								}
							}
							try { Thread.sleep(400);} catch (InterruptedException e) { e.printStackTrace(); }
						}
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
				try { Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace(); }
			}
			this.finished = true;
		}

		private boolean finished = false;
		public  boolean done() {  return finished;  }
	}


	/*************************************************************/
	/*                  Simple Behaviours                        */
	/*					  Gets and Sets    						 */
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
