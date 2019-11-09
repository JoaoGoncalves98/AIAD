package Utils;
import Utils.*;
import Agents.*;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Team {
	private ArrayList<AID> players = new ArrayList<AID>();
	private int nPlayers = 0;
	private String name;
	private Manager manager = new Manager();
	
	public Team(int nPlayers, String name) {
		this.nPlayers = nPlayers;
		this.name = name;
	}
	
	public boolean addPlayer(AID player) {
		if (this.players.size() < this.nPlayers) {
			this.players.add(player);
			this.nPlayers++;
			return true;
		}
		return false;
	}
}
