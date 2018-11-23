/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.core;

import javafx.scene.paint.Color;

public class Player {

	private Color color;
	private final String name;

	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	public String getName() {
		return name;
	}

}
