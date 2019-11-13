package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.*;

import java.io.IOException;

public class Game extends Agent {

	private Utils utils = new Utils( this );
    
    private int nPlayers = 4;
	private Team team1 = new Team(this.nPlayers/2, "A");
	private Team team2 = new Team(this.nPlayers/2, "B");
	private AID referee = null;
	private BasketballCourt court = new BasketballCourt(4, 'A', 'B', 20, 10);
	
	protected void setup() {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "game" );
        sd.setName( getLocalName() );
        this.utils.register( sd );

        /* Does first behaviour */
		addBehaviour( new gatherGameMembers(this, this.nPlayers) );

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
	/*				   Gather Game Members						 */
	/*************************************************************/
	class gatherGameMembers extends SimpleBehaviour {
		private int incPlayers = 0;
		private int incReferee = 0;
		private int incManagers = 0;
		private int nPlayers = 0;
		private Game father;
	    
	    public gatherGameMembers( Agent a, int limit) {
	    	super(a);
	    	this.father = (Game) a;
	    	this.nPlayers = limit;
	    }
	    
		public void action() 
        {
			while(this.incPlayers != this.nPlayers || this.incReferee != 1 || this.incManagers != 2 ) {
	            ACLMessage msg = receive();

	            try {
	               Thread.sleep(500);
	            }
	            catch (Exception e) {}
	            
	        	System.out.println("running game" + " players" + this.incPlayers + " ref" + this.incReferee + " mans" + this.incManagers);
	        	
	            if (msg != null) {
	            	System.out.println("game caught msg!");
	            	AID sender = msg.getSender();
	            	System.out.println(sender.getLocalName());
	                if (Utils.JOIN.equals( msg.getContent() )) {
	                	if(this.father.getTeam1().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent(Utils.JOINNED);
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
							System.out.println("SUCCESSED TO ALOCATE PLAYER TO TEAMA");
	                	} else if(this.father.getTeam2().addPlayer(sender)) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent(Utils.JOINNED);
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
							System.out.println("SUCCESSED TO ALOCATE PLAYER TO TEAMB");
	            		} else {
	                		System.out.println("FAILED TO ALOCATE PLAYER TO TEAM");
						}
	                } else if (Utils.JOINREF.equals( msg.getContent() )) {
						this.father.setReferee(sender);

						ACLMessage m = new ACLMessage( ACLMessage.INFORM );
						m.setContent(Utils.JOINNED);
						m.addReceiver( sender );

						send( m );

						this.incReferee++;

					} else if (Utils.JOINMAN.equals( msg.getContent() )) {
						if(this.father.team1.setManager( sender )) {
							ACLMessage m = new ACLMessage( ACLMessage.INFORM );
							m.setContent(Utils.JOINNED);
							m.addReceiver( sender );

							send( m );

							this.incManagers++;
						} else if(this.father.team2.setManager( sender )) {
							ACLMessage m = new ACLMessage( ACLMessage.INFORM );
							m.setContent(Utils.JOINNED);
							m.addReceiver( sender );

							send( m );

							this.incManagers++;
							this.father.team2.setManager( sender );
						} else {
							ACLMessage m = new ACLMessage( ACLMessage.INFORM );
							m.setContent(Utils.FAILED);
							m.addReceiver( sender );

							send( m );
						}
					} else {
            			System.out.println("This message wasn't suposed to come here xd");
            		}
	            } else {
	                // if no message is arrived, block the behaviour
	                block();
	            }
			}
        	System.out.println("All players were gathered");
			this.father.utils.takeDown(); // Deletes DF entry
        	this.finished = true;
        }
		
        private boolean finished = false;
        public  boolean done() { return finished; }
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
			m.setContent(Utils.STARTGAME);
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
					if (Utils.STARTEDGAME.equals( msg.getContent() )) {
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
			private boolean gameGoing = true;

			public regulatingGame( Agent a ) {
				super(a);
				this.father = (Game) a;
			}

			public void action() {

				// Função para criar game matrix
				this.father.court.initialize();

				ServiceDescription sd  = new ServiceDescription();
				sd.setType( "gamestarted" );
				sd.setName( getLocalName() );

				this.father.utils.register( sd );

				// Broadcast !!!
				while(this.gameGoing) {
				    // VE SAE JOGO JA ACABOU
                    ACLMessage msg0 = receive();
                    if (msg0 != null) {
                        //AID sender = msg.getSender();
                        //System.out.println(sender);
                        if (Utils.ENDEDGAME.equals( msg0.getContent() )) {
                            this.gameGoing = !this.gameGoing;
                            this.father.utils.takeDown(); // Deletes DF entry
                        }
                    } else {
                        // if no message is arrived, block the behaviour
                        block();
                    }
                    try { Thread.sleep(1000); } catch (Exception e) {}

                    // JOGADA AGENTE A AGENTE
                    Team team = null;
					for(int i = 0 ; i < this.father.nPlayers ; i++) {

						// IMPRIME COURT
						this.father.court.printCourt();

					    if (i%2 == 0)
					        team = this.father.team1;
					    else
                            team = this.father.team2;

                        ACLMessage m1 = new ACLMessage( ACLMessage.INFORM );
                        m1.setContent( Utils.PLAY );
						System.out.println("TEAM = " + team);
						System.out.println("TEAM PLAYERS = " + team.players);
						System.out.println("INDEX = " + i/2);
                        m1.addReceiver( team.players.get(i/2) );
                        send( m1 );
                        try {
                            m1.setContentObject( this.father.court );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        m1.addReceiver( team.players.get(i/2) );
                        send( m1 );

                        boolean f = true;
                        while(f) {
                            ACLMessage msg = receive();
                            System.out.println("GAME WAITING FOR" + team.players.get(i/2));
                            if (msg != null) {
                                AID player = msg.getSender();
								System.out.println("GAME CAUGHT MSG! From " + player.getLocalName());
								System.out.println("AND I IS = " + i);
								System.out.println("AND I/2 IS = " + i/2);
								System.out.println("AID sent to is = " + team.players.get(i/2));
								System.out.println("AID received from is = " + player);
                                if (player.getLocalName().equals(team.players.get(i / 2).getLocalName())) {
                                    String content = (String) msg.getContent();
                                    if (content.contains(Utils.RUN)) {
                                        // process run
										String[] tokens = content.split(" ");

										System.out.println(player.getLocalName() + " wants to run");
										System.out.println(player.getLocalName() + " WANTS TO RUN TO:" + Integer.parseInt(tokens[1]) + " " + Integer.parseInt(tokens[2]));

										if(this.father.court.updatePos(player.getLocalName(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])))
											System.out.println(player.getLocalName() + " and ruunnnnnnnnnn");
										else
											System.out.println(player.getLocalName() + " but didnt ruuuuuuuuun");

                                        f = false;
                                    } else if (content.contains(Utils.PASS)) {
                                        // process pass
										String[] tokens = content.split(" ");

										System.out.println(player.getLocalName() + " wants to pass");
										System.out.println(player.getLocalName() + " WANTS TO PASS TO:" + Integer.parseInt(tokens[1]) + " WITH PASS STAT OF " + Integer.parseInt(tokens[2]));

										if(this.father.court.updatePos(player.getLocalName(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])))
											System.out.println(player.getLocalName() + " and ruunnnnnnnnnn");
										else
											System.out.println(player.getLocalName() + " but didnt ruuuuuuuuun");

                                        f = false;
                                    } else if (content.contains(Utils.LAUNCH)) {
                                        // process launch
                                        System.out.println(player.getLocalName() + " wants to launch");
                                        f = false;
                                    }
                                } else {
                                    System.out.println("Wrong player to choose play!");
                                }
                            } else {
                                // if no message is arrived, block the behaviour
                                block();
                            }
                            try { Thread.sleep(1000); } catch (Exception e) {}
                        }
                    }
                }
				this.finished = true;
			}


			private boolean finished = false;
		public  boolean done() {  return finished;  }
	}
}

/*
			While ref hasn't ended game
			For each player 1 to N
			team1
			team2

			send court
								recieve
								choose play
								send play
			recieve play
			see if possible
			If has prob calculate else skip
			Send success or failure
								recieve answer

			For each manager
				Send Results
				React to results sending messages to players
*/