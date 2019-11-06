package Utils;
import Utils.*;
import Agents.*;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.AID;

public class Team {
	private ArrayList<AID> players = new ArrayList<AID>();
	private ArrayList<Player> playersInfo = new ArrayList<>(); //ainda esta vazio
	private int nPlayers = 0;
	private String name;
	private boolean hasPosession=false;
	private int difScore = 0;
	private String tactics = "";

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


	public int passBall() {
		if (hasPosession) {
			for (Player player : playersInfo) {
				//Podia fazer aqui uma %de acerto de passe de 85% e tb mudar se houver alguem pelo caminho
				if(!player.hasBall())
				{
					player.setHasBall(true);
					Ball.position=player.getPosition();
				}
				else
				{
					player.setHasBall(false);
				}
			}

			//then change the ball position to the player who has the ball
			return 0;
		}
		return -1;
	}

	public int shootBall() {
		if (hasPosession) {
			for (Player player : playersInfo) {
				if(player.hasBall())
				{
					if(player.basketRange()==0 && Math.random()*100 <= player.getStats().getInsideScorePercentage())
					{
						difScore += 2;
						return 2;
					}
					if(player.basketRange()==1 && Math.random()*100 <= player.getStats().getOutsideScorePercentage())
					{
						difScore += 3;
						return 3;
					}
					if(player.basketRange()==2 && Math.random()*100 <= 5) //every player has a 5% chance of hitting a long range shot
					{
						difScore += 3;
						return 3;
					}

					//then change posession to other team player and the ball position to that player
					player.setHasBall(false);
					hasPosession=false;
					return 0;
				}
			}
		}
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
