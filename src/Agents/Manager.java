package Agents;

import jade.core.Agent;
import jade.core.behaviours.*;
import Utils.Position;
import Utils.Stats;
import jade.core.AID;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Manager extends Agent {
    
	protected void setup()
    {
        addBehaviour( new manageTeam(this) );
    }
/*************************************************************/
    /*                  Simple Behaviours                        */
    /*************************************************************/
    class manageTeam extends SimpleBehaviour {
        private Player father;
        private AID agentGame = null;

        public manageTeam( Agent a ) {
            super(a);
            this.father = (Player) a;
        }

        public void action()
        {
            System.out.println("MANAGER IS HERE!!!!!!!!!!!");
            // Check if Game started
            while(this.agentGame == null) {

                System.out.println("game started?");

                this.agentGame = getService("gamestarted");

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