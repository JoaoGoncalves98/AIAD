package ToRun;

import Agents.Game;
import Agents.Manager;
import Agents.Player;
import Agents.Referee;
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
        // ContainerController containerController = new ContainerController();

        for(int i = 0 ; i < 3 ; i++)
            for(int j = 0 ; j < 3 ; j++)
                for(int x = 0 ; x < 3 ; x++)
                    for(int y = 0 ; y < 3 ; y++) {
                        runtime = jade.core.Runtime.instance();

                        Profile profile = new ProfileImpl();
                        profile.setParameter("gui","false");
                        profile.setParameter("port","8000");

                        container = (AgentContainer) runtime.createMainContainer(profile);
                        try {
                            // CREATING GAME
                            AgentController gameController = container.createNewAgent("g0", "Agents.Game", null);
                            gameController.start();

                            // CREATING REFEREE
                            AgentController refereeController = container.createNewAgent("ref01", "Agents.Referee", null);
                            refereeController.start();

                            // CREATING MANAGERS
                            AgentController managerController = container.createNewAgent("mA", "Agents.Manager", null);
                            managerController.start();
                            managerController = container.createNewAgent("mB", "Agents.Manager", null);
                            managerController.start();

                            // CREATING PLAYERS
                            AgentController playerController = container.createNewAgent("a1", "Agents.Player", new Object[] {i});
                            playerController.start();
                            playerController = container.createNewAgent("a2", "Agents.Player", new Object[] {j});
                            playerController.start();
                            playerController = container.createNewAgent("b1", "Agents.Player", new Object[] {x});
                            playerController.start();
                            playerController = container.createNewAgent("b2", "Agents.Player", new Object[] {y});
                            playerController.start();
                        } catch (StaleProxyException e) {

                        }

                        try {
                            Thread.sleep(22 * 1000); //TEMPO DE JOGO SEM TIMERS FICA A QUANTO HMMMM?
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

        /*
        // CREATING GAME
        Game g00 = new Game();
        container.acceptNewAgent("g00", g00).start();

        // CREATING REFEREE
        Referee ref00 = new Referee();
        container.acceptNewAgent("ref00", ref00).start();

        // CREATING MANAGERS
        Manager manA00 = new Manager();
        container.acceptNewAgent("mA", manA00).start();
        Manager manB00 = new Manager();
        container.acceptNewAgent("mB", manA00).start();

        // CREATING PLAYERS
        Player a1 = new Player();
        container.acceptNewAgent("a1", a1).start();
        //...

        // AgentController g0 = container.createNewAgent("g0", "Agents.Game", null);
        // g0.start();
        */

        // to run once! on main-container
        // ContainerController containerController = getContainerController();
/*
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
            AgentController playerController = containerController.createNewAgent("a1", "Agents.Player", new Object[] {new Integer(1)});
            playerController.start();
            playerController = containerController.createNewAgent("a2", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("b1", "Agents.Player", null);
            playerController.start();
            playerController = containerController.createNewAgent("b2", "Agents.Player", null);
            playerController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }*/
    }
}
