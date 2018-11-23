/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.core.model;

import ca.uqac.gomoku.core.Player;

public class Spot {

	public final int x, y;

	private Player occupant;

	Spot(int x, int y) {
		this.x = x;
		this.y = y;
	}

	void clear() {
		occupant = null;
	}

	void setOccupant(Player player) {
		this.occupant = player;
	}

	public Player getOccupant() {
		return occupant;
	}
	
	public boolean isOccupant(Player player) {
		return occupant == player;
	}

	public boolean isEmpty() {
		return occupant == null;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}
}
