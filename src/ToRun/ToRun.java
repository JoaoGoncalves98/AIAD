package ToRun;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.*;

public class ToRun  extends Agent {
    private static Runtime runtime;
    private static AgentContainer container;

    protected void setup() {

        // ContainerController newContainer = new ContainerController();

        /*
        runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter("gui","false");
        profile.setParameter("port","8000");

        container = (AgentContainer) runtime.createMainContainer(profile);

        try {
            container.acceptNewAgent("name", agent).start(); //
        } catch (StaleProxyException e) {

        }

        */

        ContainerController containerController = getContainerController();

        try {
            // CREATING GAME
            AgentController gameController = containerController.createNewAgent("g0", "Agents.Game", null);
            gameController.start();

            // CREATING REFEREE
            AgentController refereeController = containerController.createNewAgent("ref01", "Agents.Referee", null);
            refereeController.start();

            // CREATING MANAGERS
            AgentController managerController = containerController.createNewAgent("mA", "Agents.Manager", null);
            managerController.start();
            managerController = containerController.createNewAgent("mB", "Agents.Manager", null);
            managerController.start();

            // CREATING PLAYERS
            AgentController playerController = containerController.createNewAgent("a1", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("a2", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("b1", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("b2", "Agents.Player", null);
            playerController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
