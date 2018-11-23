/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.ui;

import ca.uqac.gomoku.core.Player;
import ca.uqac.gomoku.core.model.Grid;
import ca.uqac.gomoku.core.model.Spot;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

	private Grid gameGrid;
	private Player players[];
	int turn = 0;
	Canvas playGround;

	@Override
	public void init() throws Exception {
		super.init();
		players = new Player[]{new Player("Popeye", Color.GREEN),
								new Player("Brutus", Color.RED)};
		gameGrid = new Grid(15);
		playGround = new Board(this.gameGrid, this::makeMove);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Gomoku Game!");
		BorderPane root = new BorderPane();
		root.setCenter(playGround);
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void makeMove(Spot place) {
		gameGrid.placeStone(place.x, place.y, getCurrentPlayer());
	}

	private Player getCurrentPlayer() {
		turn++;
		return players[turn % 2];
	}

	public static void main(String[] args) {
		launch(args);
	}
}
