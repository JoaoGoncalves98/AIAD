package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
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

        public manageTeam( Agent a ) {
            super(a);
            this.father = (Manager) a;
        }

        public void action()
        {
            System.out.println("MANAGER IS HERE!!!!!!!!!!!");
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
                System.out.println("running manager");

            }
            // this.finished = true;
        }

        private boolean finished = false;
        public  boolean done() {  return finished;  }
    }
}