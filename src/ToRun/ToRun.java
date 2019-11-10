package ToRun;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ToRun  extends Agent {
    protected void setup() {

        ContainerController containerController = getContainerController();

        try {
            // CREATING GAME
            AgentController gameController = containerController.createNewAgent("g0", "Agents.Game", null);
            gameController.start();

            // CREATING REFEREE
            AgentController refereeController = containerController.createNewAgent("ref01", "Agents.Referee", null);
            refereeController.start();

            // CREATING MANAGERS
            AgentController managerController = containerController.createNewAgent("man01", "Agents.Manager", null);
            managerController.start();
            managerController = containerController.createNewAgent("man02", "Agents.Manager", null);
            managerController.start();

            // CREATING PLAYERS
            AgentController playerController = containerController.createNewAgent("p01", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("p02", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("p03", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("p04", "Agents.Player", null);
            playerController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
