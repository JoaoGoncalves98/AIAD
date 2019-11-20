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
