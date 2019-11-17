package Utils;
import Utils.*;
import Agents.*;

import java.util.ArrayList;

import jade.core.AID;

public class Team {
	public ArrayList<AID> players = new ArrayList<AID>();
	private ArrayList<Player> playersInfo = new ArrayList<>(); //ainda esta vazio
	private int nPlayers = 0;
	private String name;
	private boolean hasPosession=false;
	private int difScore = 0;
	private String tactics = "";

	private AID manager = null;
	
	public Team(int nPlayers, String name) {
		this.nPlayers = nPlayers;
		this.name = name;
	}
	
	public boolean addPlayer(AID player) {
		System.out.println("PLAYER TRYING TO ENTER TEAM " +  this.name.toLowerCase() + " IS " + player.getLocalName().toLowerCase());
		System.out.println("CONDITION CONTAINS SAYS: " + player.getLocalName().toLowerCase().contains(this.name.toLowerCase()));
		if(player.getLocalName().toLowerCase().contains(this.name.toLowerCase()))
			if (this.players.size() < this.nPlayers) {
				System.out.println("TEAM " + name + "ADDED PLAYER " + player.getLocalName());
				this.players.add(player);
				return true;
			}
		return false;
	}

	public boolean setManager ( AID man ) {
		if(!this.hasManager() &&  man.getLocalName().toLowerCase().contains(this.name.toLowerCase())) {
			this.manager = man;
			return true;
		}
		return false;
	}
	public AID getManager () {
		return this.manager;
	}
	public boolean hasManager () {
		if(this.manager != null)
			return true;
		return false;
	}

	public ArrayList<AID> getPlayers() {
		return players;
	}

	public boolean hasPosession() {
		return hasPosession;
	}

	public void setHasPosession(boolean hasPosession) {
		this.hasPosession = hasPosession;
	}

	public int getDifScore() {
		return difScore;
	}

	public void setDifScore(int difScore) {
		this.difScore = difScore;
	}

	public String getTactics() {
		return tactics;
	}


	public int setTactics(String tactics) {
		return -1;
	}

	public boolean movePlayer(Player player, char direction) {
		//doesn't check if there is an opponent in the space
		switch(direction)
		{
			case 'u':
			{
				for (Player allPlayer: playersInfo)
				{
					if(allPlayer.getPosition().getX()==player.getPosition().getX() && allPlayer.getPosition().getY()==player.getPosition().getY()-1)
						return false;
				}
				if(player.getPosition().getY()>0)
				{
					player.setPosition(new Position(player.getPosition().getX(),player.getPosition().getY()-1));
					return true;
				}
				return false;
			}
			case 'd':
			{
				for (Player allPlayer: playersInfo)
				{
					if(allPlayer.getPosition().getX()==player.getPosition().getX() && allPlayer.getPosition().getY()==player.getPosition().getY()+1)
						return false;
				}
				if(player.getPosition().getY()<10)
				{
					player.setPosition(new Position(player.getPosition().getX(),player.getPosition().getY()+1));
					return true;
				}
				return false;
			}
			case 'l':
			{
				for (Player allPlayer: playersInfo)
				{
					if(allPlayer.getPosition().getX()==player.getPosition().getX()-1 && allPlayer.getPosition().getY()==player.getPosition().getY())
						return false;
				}
				if(player.getPosition().getY()>0)
				{
					player.setPosition(new Position(player.getPosition().getX()-1,player.getPosition().getY()));
					return true;
				}
				return false;
			}
			case 'r':
			{
				for (Player allPlayer: playersInfo)
				{
					if(allPlayer.getPosition().getX()==player.getPosition().getX()+1 && allPlayer.getPosition().getY()==player.getPosition().getY())
						return false;
				}
				if(player.getPosition().getY()>0)
				{
					player.setPosition(new Position(player.getPosition().getX()+1,player.getPosition().getY()));
					return true;
				}
				return false;
			}
			default: return false;
		}
	}


}
