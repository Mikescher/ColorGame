package org.ColorGame.logic;

import org.ColorGame.ColorGameState;

public class TimeKeeper {

	private long startTime; // ms
	private int turnCount;
	private long maxTime;   // ms
	
	private boolean gameOver;
	
	public TimeKeeper() {
		restart();
	}
	
	public void restart() {
		startTime = now();
		turnCount = 0;
		maxTime = ColorGameState.TIME_INITIAL * 1000;
		gameOver = false;
	}

	private long now() {
		return System.currentTimeMillis();
	}
	
	public double getElapsedTime() {
		return (now() - startTime) / 1000.0;
	}

	public int getTurnCount() {
		return turnCount;
	}

	public double getMaxTime() {
		return maxTime / 1000.0;
	}

	public void doTurn() {
		update();
		
		turnCount++;
		if (turnCount >= ColorGameState.TURN_COUNT) {
			long rem_time = (startTime + maxTime) - now();
			turnCount = 0;
			rem_time += ColorGameState.TIME_BONUS * 1000;
			
			startTime = now();
			maxTime = rem_time;
		}
	}
	
	private void update() {
		if (getElapsedTime() > getMaxTime()) gameOver = true;
	}

	public boolean isGameOver() {
		update();
		
		return gameOver;
	}
}