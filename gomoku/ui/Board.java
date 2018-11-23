/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.ui;

import ca.uqac.gomoku.core.BoardEventListener;
import ca.uqac.gomoku.core.GridEventListener;
import ca.uqac.gomoku.core.Player;
import ca.uqac.gomoku.core.model.Grid;
import ca.uqac.gomoku.core.model.Spot;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Board extends Canvas {

	/** Number of pixels between horizontal/vertical lines of the board. */
	private final double spotSize = 30;
	private Grid boardModel;

	public Board(Grid b, BoardEventListener lstnr) {
		setWidth(spotSize * b.getSize());
		setHeight(spotSize * b.getSize());
		this.boardModel = b;
		b.addGridEventstListener(new GridEventListener() {
			@Override
			public void stonePlaced(Spot place) {
				drawStone(place);
			}
			@Override
			public void gameOver(Player winner) {
				System.out.println("bingoooo! the " + winner.getName() + " wins");
			}
		});

		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Spot place = locateSpot((int) e.getX(), (int) e.getY());
			if (place != null && place.isEmpty()) {
				lstnr.spotClicked(place);
			}
		});
		drawGrid();
	}

	private void drawGrid() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.setFill(Color.ANTIQUEWHITE);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);

		// number of v/h lines including boarder lines
		final int lines = boardModel.getSize() + 2;
		gc.fillRect(0, 0, spotSize * (lines - 1), spotSize * (lines - 1));

		double x = 0; // placeSize;
		for (int i = 0; i < lines; i++) {
			gc.strokeLine(x, 0, x, spotSize * (lines - 1));
			x += spotSize;
		}

		double y = 0; // placeSize;
		for (int i = 0; i < lines; i++) {
			gc.strokeLine(0, y, spotSize * (lines - 1), y);
			y += spotSize;
		}
	}

	@SuppressWarnings("unused")
	private void drawStones() {
		boardModel.getSpots().forEach(s -> drawStone(s));
	}

	private void drawStone(Spot s) {
		if (!s.isEmpty()) {
			GraphicsContext gc = this.getGraphicsContext2D();
			gc.setFill((s.getOccupant()).getColor());
			double x = spotSize + s.x * spotSize; // center x
			double y = spotSize + s.y * spotSize; // center y
			double r = spotSize / 2; // radius
			gc.fillOval(x - r, y - r, r * 2, r * 2);
		}
	}

	private Spot locateSpot(double x, double y) {
		final double boardSize = boardModel.getSize();
		final double border = spotSize; // * 1;
		// recognize R pixels from an intersection
		final double R = spotSize / 2 - 2;

		// off board?
		if (x < border - R || y < border - R
				|| x > spotSize * (boardSize + 1) - (spotSize - R)
				|| y > spotSize * (boardSize + 1) - (spotSize - R)) {
			return null;
		}

		double px = 0;
		double dx = (x - border) % spotSize;
		if (dx <= R) {
			px = (x - border) / spotSize;
		} else if (dx >= spotSize - R) {
			px = (x - border) / spotSize + 1;
		} else {
			return null;
		}

		double py = 0;
		double dy = (y - border) % spotSize;
		if (dy <= R) {
			py = (y - border) / spotSize;
		} else if (dy >= spotSize - R) {
			py = (y - border) / spotSize + 1;
		} else {
			return null;
		}
		return boardModel.getSpot((int) px, (int) py);
	}

}
