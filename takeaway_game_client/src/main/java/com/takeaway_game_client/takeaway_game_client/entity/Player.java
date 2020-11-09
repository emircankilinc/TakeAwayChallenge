package com.takeaway_game_client.takeaway_game_client.entity;

import java.io.Serializable;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7963796400462297722L;

	private Integer playerId;
	private Integer rivalId;
	private MoveState moveState;
	private Integer nextValue;

	
	
	public Player() {
		super();
	}

	public Player(Integer playerId, Integer rivalId, MoveState moveState, Integer nextValue) {
		super();
		this.playerId = playerId;
		this.rivalId = rivalId;
		this.moveState = moveState;
		this.nextValue = nextValue;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getRivalId() {
		return rivalId;
	}

	public void setRivalId(Integer rivalId) {
		this.rivalId = rivalId;
	}

	public MoveState getMoveState() {
		return moveState;
	}

	public void setMoveState(MoveState moveState) {
		this.moveState = moveState;
	}

	public Integer getNextValue() {
		return nextValue;
	}

	public void setNextValue(Integer nextValue) {
		this.nextValue = nextValue;
	}

}
