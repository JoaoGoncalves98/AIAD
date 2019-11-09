package Agents;

import Utils.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;

public class Manager extends Agent {

    private Utils utils = new Utils( this );
    
	protected void setup()
    {
        addBehaviour( new manageTeam(this) );
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