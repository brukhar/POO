/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.core;

import ca.uqac.gomoku.core.model.Spot;

public interface GridEventListener {
	void stonePlaced(Spot place);
	void gameOver(Player winner);
}
