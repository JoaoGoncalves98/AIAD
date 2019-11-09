package Utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Utils {
    public final static String JOIN = "JOIN";
    public final static String JOINREF = "JOINREF";
    public final static String JOINMAN = "JOINREF";
    public final static String JOINNED = "JOINMAN";
    public final static String FAILED = "FAILED";
    public final static String STARTGAME = "STARTGAME";
    public final static String STARTEDGAME = "STARTEDGAME";
    public final static String ENDEDGAME = "ENDEDGAME";

    private Agent agent = null;

    public Utils( Agent a) {
        this.agent = a;
    }

    /*************************************************************/
    /*                     DF managing                           */
    /*************************************************************/
    public void register( ServiceDescription sd )
//  ---------------------------------
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.agent.getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this.agent, dfd );
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }

    protected void takeDown()
//  ---------------------------------
    {
        try { DFService.deregister( this.agent ); }
        catch (Exception e) {}
    }

    public AID getService(String service )
//  ---------------------------------
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        dfd.addServices(sd);
        try
        {
            DFAgentDescription[] result = DFService.search(this.agent, dfd);
            if (result.length>0)
                return result[0].getName() ;
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
        return null;
    }

}
