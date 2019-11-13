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
    public final static String JOINMAN = "JOINMAN";
    public final static String JOINNED = "JOINMAN";
    public final static String FAILED = "FAILED";
    public final static String STARTGAME = "STARTGAME";
    public final static String STARTEDGAME = "STARTEDGAME";
    public final static String ENDEDGAME = "ENDEDGAME";
    public final static String PLAY = "PLAY";
    public final static String RUN = "RUN";
    public final static String PASS = "PASS";
    public final static String LAUNCH = "LAUNCH";
    public final static String SCORE = "SCORE"; // Mensagens a usar nos managers
    public final static String AGRESSIVE = "AGRESSIVE";
    public final static String PASSIVE = "PASSIVE";
    public final static String ACK = "ACK";

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

    public void takeDown()
//  ---------------------------------
    {
        try {
            DFService.deregister( this.agent );
            System.out.println("DESREGISTOU CRL.....................");
        }
        catch (Exception e) {
            System.out.println("NÃO DESREGISTOU CRL.....................");
        }
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
