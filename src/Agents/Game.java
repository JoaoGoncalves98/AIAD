package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Game extends Agent {

	private Utils utils = new Utils( this );
    
    private int nPlayers = 4;
	private Team team1 = new Team(this.nPlayers/2, "A");
	private Team team2 = new Team(this.nPlayers/2, "B");
	private AID referee = null;
	private BasketballCourt court = new BasketballCourt(4, 'A', 'B', 8, 6);

	private int[] score = new int[2]; // Pontuação
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
	               Thread.sleep(1);
	            }
	            catch (Exception e) {}
	            
	        	
	            if (msg != null) {
	            	AID sender = msg.getSender();
                    if (msg.getContent().contains(Utils.JOIN + " ")) {

                        String[] tokens = msg.getContent().split(" ");
                        System.out.println("PLAYER: " + sender + " of type " + Integer.parseInt(tokens[1]));

                        if(this.father.getTeam1().addPlayer(sender, Integer.parseInt(tokens[1]))) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent(Utils.JOINNED);
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
							System.out.println("SUCCESSED TO ALOCATE PLAYER TO TEAM A");
	                	} else if(this.father.getTeam2().addPlayer(sender, Integer.parseInt(tokens[1]))) {
	                		ACLMessage m = new ACLMessage( ACLMessage.INFORM );
	                        m.setContent(Utils.JOINNED);
	                        m.addReceiver( sender );
	
	                        send( m );
	                        
	                        this.incPlayers++;
							System.out.println("SUCCESSED TO ALOCATE PLAYER TO TEAM B");
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
					Thread.sleep(1);
				}
				catch (Exception e) {}

				ACLMessage msg = receive();

				if (msg != null) {
					AID sender = msg.getSender();
					if (Utils.STARTEDGAME.equals( msg.getContent() )) {
						this.flag = !this.flag;
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

				this.father.score[0] = 0; // Equipa A
				this.father.score[1] = 0; // Equipa B

				ServiceDescription sd  = new ServiceDescription();
				sd.setType( "gamestarted" );
				sd.setName( getLocalName() );

				this.father.utils.register( sd );

				// Broadcast !!!
                PrintWriter out;
                PrintWriter out2;
				try{
                    out = new PrintWriter(new BufferedWriter(new FileWriter("logs/rapidData.csv", true)));
                    out2 = new PrintWriter(new BufferedWriter(new FileWriter("logs/rapidData2.csv", true)));

                    out2.write("A" + ", " + this.father.team1.getTypes().get(0) + ", " + this.father.team1.getTypes().get(1) + ", " + "B" + ", " + this.father.team2.getTypes().get(0) + ", " + this.father.team2.getTypes().get(1) + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
                    out2.flush();
					while(this.gameGoing) {
				    // VE SE JOGO JA ACABOU
                    ACLMessage msg0 = receive();
                    if (msg0 != null) {
                        this.endgame(msg0);
                    } else {
                        // if no message is arrived, block the behaviour
                        block();
                    }
                    try { Thread.sleep(1); } catch (Exception e) {}

                    // JOGADA AGENTE A AGENTE
                    Team team = null;

					/*MENSAGEM PARA OS MANAGERS DAS DUAS EQUIPAS*/
					for (int i = 0; i < 2; i++)
					{
						if (i%2 == 0) {
							team = this.father.team1;
						}
						else {
							team = this.father.team2;
						}

						ACLMessage m1 = new ACLMessage( ACLMessage.INFORM );

						m1.setContent( Utils.SCORE);
						m1.addReceiver(team.getManager());
						send(m1);
						try {
							m1.setContentObject( this.father.score );
						} catch (IOException e) {
							e.printStackTrace();
						}
						m1.addReceiver( team.getManager() );
						send( m1 );

						boolean f = true;
						while(f)
						{
							ACLMessage msg = receive();
							if(msg != null)
							{
                                this.endgame(msg);
								if(Utils.ACK.equals(msg.getContent()))
								{
									f = false;
								}
							}
						}
					}
					/*----------------------------------------------*/

					for(int i = 0 ; i < this.father.nPlayers ; i++) {

						// IMPRIME COURT
						this.father.court.printCourt();

					    if (i%2 == 0)
					        team = this.father.team1;
					    else
                            team = this.father.team2;

                        ACLMessage m1 = new ACLMessage( ACLMessage.INFORM );
                        m1.setContent( Utils.PLAY );
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
                        long startTime1 = System.currentTimeMillis(); //time out a second and a half
                        while(f && (System.currentTimeMillis()-startTime1)<1500) {
                            ACLMessage msg = receive();
                            if (msg != null) {

                                AID player = msg.getSender();
                                this.endgame(msg);
                                if (player.getLocalName().equals(team.players.get(i / 2).getLocalName())) {
									//try { Thread.sleep(10); } catch (Exception e) {}
									String content = null;
                                    long startTime2 = System.currentTimeMillis(); //time out a second and a half
                                    while(content == null && (System.currentTimeMillis()-startTime2)<1500)
										content = (String) msg.getContent();
                                    if(content == null)
                                        content = "none";
									System.out.println("------------------>content: " + content);
                                    if (content.contains(Utils.RUN)) {
                                        // process run
										String[] tokens = content.split(" ");

										System.out.println(player.getLocalName() + " wants to run to position: i=" + Integer.parseInt(tokens[1]) + " j=" + Integer.parseInt(tokens[2]));

										if(this.father.court.updatePos(player.getLocalName(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])))
											System.out.println(player.getLocalName() + " moved successfully");
										else
											System.out.println(player.getLocalName() + " couldn't move");


                                        out.write("A, false, false, " + Manager.aTactic + ", B, false, false, " + Manager.bTactic + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
                                        out.flush();

                                        f = false;
                                    } else if (content.contains(Utils.PASS)) {
                                        // process pass
										String[] tokens = content.split(" ");
										System.out.println(player.getLocalName() + " wants to pass to: " + tokens[1] + " with a pass rating of: " + Integer.parseInt(tokens[2]));


										if(this.father.court.makePass(player.getLocalName(), tokens[1], Integer.parseInt(tokens[2])))
											System.out.println(player.getLocalName() + " and passed successfully");
										else
											System.out.println(player.getLocalName() + " and the pass was missed");

                                        f = false;
											if(player.getLocalName().toLowerCase().contains(""+'a'))
											{
												System.out.println("\n\nteste: " + player.getLocalName().toLowerCase());
												out.write("A, true, false, " + Manager.aTactic + ", B, false, false, " + Manager.bTactic + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
												out.flush();
											}
											else
											{
												out.write("A, false, false, " + Manager.aTactic + ", B, true, false, " + Manager.bTactic + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
												out.flush();
											}
                                    } else if (content.contains(Utils.LAUNCH)) {
                                        // process launch
										String[] tokens = content.split(" ");
										System.out.println(player.getLocalName() + " wants to shoot");
										if(player.getLocalName().toLowerCase().contains(""+'a'))
											this.father.score[0]+=this.father.court.shootBall(player.getLocalName(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
										else
											this.father.score[1]+=this.father.court.shootBall(player.getLocalName(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));

                                        f = false;

											if(player.getLocalName().toLowerCase().contains(""+'a'))
											{
												System.out.println("\n\nteste: " + player.getLocalName().toLowerCase());
												out.write("A, false, true, " + Manager.aTactic + ", B, false, false, " + Manager.bTactic + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
												out.flush();
											}
											else
											{
												System.out.println("\n\nteste: " + player.getLocalName().toLowerCase());
												out.write("A, false, false, " + Manager.aTactic + ", B, false, true, " + Manager.bTactic + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0]-this.father.score[1])+ ",\n");
												out.flush();
											}

                                    }

                                } else {
                                    System.out.println("player to play: " + team.players.get(i / 2).getLocalName());
                                    System.out.println("player that tried to play: " + player.getLocalName());
                                    System.out.println("Wrong player to choose play!");
                                    //f = false;
                                }
                            } else {
                                // if no message is arrived, block the behaviour
                                block();
                            }
                            try { Thread.sleep(1); } catch (Exception e) {}
                        }
                    }
                }
				}
				catch (IOException e) {
					//exception handling left as an exercise for the reader
				}
				this.finished = true;
			}

			public boolean endgame( ACLMessage msg0 ) {
                if (Utils.ENDEDGAME.equals( msg0.getContent() )) {
                    this.gameGoing = !this.gameGoing;


                    PrintWriter out2;
                    try {
                        out2 = new PrintWriter(new BufferedWriter(new FileWriter("logs/rapidData2.csv", true)));
                        out2.write("A" + ", " + this.father.team1.getTypes().get(0) + ", " + this.father.team1.getTypes().get(1) + ", " + "B" + ", " + this.father.team2.getTypes().get(0) + ", " + this.father.team2.getTypes().get(1) + ", " + this.father.court.hasPossession() + ", " + (this.father.score[0] - this.father.score[1]) + ",\n");
                        out2.flush();
                    } catch (IOException e) {

                    }

                    this.father.utils.takeDown(); // Deletes DF entry
					return true;
                }
                return false;
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