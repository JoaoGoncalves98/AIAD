package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Manager extends Agent {

    private Utils utils = new Utils( this );
    
	protected void setup()
    {
        /* Does first behaviour */
        addBehaviour( new joiGame(this) );

        /* Does second behaviour */
        addBehaviour( new manageTeam(this) );
    }


    /*************************************************************/
    /*                  Simple Behaviours                        */
    /*					   Join Game    						 */
    /*************************************************************/
    class joiGame extends SimpleBehaviour {
        private Manager father;
        private AID agentGame = null;
        private boolean joinned = false;

        public joiGame( Agent a ) {
            super(a);
            this.father = (Manager) a;
        }

        public void action()
        {
            // Search Game
            while(this.agentGame == null) {

                System.out.println("searching game");

                this.agentGame = this.father.utils.getService("game");

                ACLMessage m = new ACLMessage( ACLMessage.INFORM );
                m.setContent(Utils.JOINMAN);
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
                System.out.println("running manager");

                ACLMessage msg = receive();

                if (msg != null) {
                    System.out.println("manager caught msg!");
                    AID sender = msg.getSender();
                    if (Utils.JOINNED.equals( msg.getContent() )) {
                        System.out.println("Joined game!");
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
    /*************************************************************/
    class manageTeam extends SimpleBehaviour {
        private Manager father;
        private AID agentGame = null;
        private boolean agressive = false;

        public manageTeam( Agent a ) {
            super(a);
            this.father = (Manager) a;
        }

        public void action()
        {
            System.out.println("MANAGER IS HERE!!!!!!!!!!!");
            // Check if Game started
            while(this.agentGame == null) {

                System.out.println("game started? -- MAN");

                this.agentGame = this.father.utils.getService("gamestarted");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (getLocalName().contains("A")) {
                ServiceDescription sd0 = new ServiceDescription();
                sd0.setName(getLocalName());
                sd0.setType(Utils.TATICS + " A " + Utils.AGRESSIVE);
                this.father.utils.register(sd0);
            } else {
                ServiceDescription sd0 = new ServiceDescription();
                sd0.setName(getLocalName());
                sd0.setType(Utils.TATICS + " B " + Utils.AGRESSIVE);
                this.father.utils.register(sd0);
            }
            /*CICLO PARA SELECIONAR A TATICA A USAR*/
            while(this.agentGame != null) {
                this.agentGame = this.father.utils.getService("gamestarted");
                ACLMessage msg = receive();
                if(msg != null)
                {
                    if (Utils.SCORE.equals(msg.getContent())) {
                        boolean f = true;
                        while (f) {
                            ACLMessage msg2 = receive();
                            if (msg2 != null)
                            {
                                try
                                {
                                    System.out.println("MANAGER OF TEAM " + "" + " GOT MESSAGE");
                                    int[] score = (int[]) msg2.getContentObject();
                                    System.out.println("SCORE: A:" + score[0] + " B:" + score[1]);

                                    //  react
                                    if (getLocalName().contains("A")) {
                                        ServiceDescription sd  = new ServiceDescription();
                                        sd.setName( getLocalName() );
                                        if(score[0] <= score[1] && !this.agressive) {
                                            this.father.utils.takeDown();
                                            sd.setType( Utils.TATICS + " A " + Utils.AGRESSIVE);
                                            this.agressive = true;
                                            this.father.utils.register( sd );
                                        } else if (score[0] > score[1] && this.agressive) {
                                            this.father.utils.takeDown();
                                            sd.setType( Utils.TATICS + " A " + Utils.PASSIVE );
                                            this.agressive = false;
                                            this.father.utils.register( sd );
                                        }
                                    } else {
                                        ServiceDescription sd  = new ServiceDescription();
                                        sd.setName( getLocalName() );
                                        if(score[0] <= score[1] && !this.agressive) {
                                            this.father.utils.takeDown();
                                            sd.setType( Utils.TATICS + " B " + Utils.AGRESSIVE );
                                            this.agressive = true;
                                            this.father.utils.register( sd );
                                        } else if (score[0] > score[1] && this.agressive) {
                                            this.father.utils.takeDown();
                                            sd.setType( Utils.TATICS + " B " + Utils.PASSIVE );
                                            this.agressive = false;
                                            this.father.utils.register( sd );
                                        }
                                    }

                                    ACLMessage m1 = new ACLMessage( ACLMessage.INFORM );
                                    m1.setContent( Utils.ACK );
                                    m1.addReceiver(this.agentGame);
                                    send(m1);
                                    f = false;
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            this.finished = true;
        }

        private boolean finished = false;
        public  boolean done() {  return finished;  }
    }
}