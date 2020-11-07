package com.takeaway.takeaway_game.entity;

import java.io.Serializable;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7963796400462297722L;
	
	
	private Integer playerId;
	private Integer rivalId;
	private MoveState moveState;
	private String nextValue;

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

	public String getNextValue() {
		return nextValue;
	}

	public void setNextValue(String nextValue) {
		this.nextValue = nextValue;
	}

}
