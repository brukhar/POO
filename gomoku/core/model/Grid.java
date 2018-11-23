/*
 * ----------------------------------------------------------------------------
 * "THE THE-WARE LICENSE":
 * Jebril A.R. wrote this bit of code.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy/make me a cup of Tea in return.
 * ----------------------------------------------------------------------------
 */
package ca.uqac.gomoku.core.model;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.gomoku.core.GridEventListener;
import ca.uqac.gomoku.core.Player;

public class Grid {
	static private int DEFAULT_BOARD_SIZE = 15;
	private int size;

	private final List<Spot> spotLst;
	private List<Spot> winningStones = new ArrayList<>(0);

	private final List<GridEventListener> listeners;

	public Grid() {
		this(DEFAULT_BOARD_SIZE);
	}
	public Grid(int size) {
		this.size = size;
		listeners = new ArrayList<>();
		spotLst = new ArrayList<>(size * size);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				spotLst.add(new Spot(x, y));
			}
		}
	}

	/** Clear this board by removing all its stones. */
	public void clear() {
		winningStones.clear();
		spotLst.forEach(p -> p.clear());
		notifyStonePlaced(null);
	}

	public int getSize() {
		return size;
	}

	public boolean isFull() {
		return spotLst.stream().allMatch(p -> !p.isEmpty());
	}

	public boolean isGameOver() {
		return winningStones.size() > 0 || isFull();
	}

	public boolean isEmpty(int x, int y) {
		return getSpot(x, y).isEmpty();
	}

	public boolean isOccupiedBy(int x, int y, Player player) {
		return getSpot(x, y).isOccupant(player);
	}

	/** Return the specified place or null if it doesn't exist. */
	public Spot getSpot(int x, int y) {
		return spotLst.stream().filter(p -> p.x == x && p.y == y).findAny()
				.get();
	}

	/** Return the places of this board. */
	public Iterable<Spot> getSpots() {
		return spotLst;
	}

	public void placeStone(int x, int y, Player player) {
		Spot place = getSpot(x, y);
		if (place != null) {
			place.setOccupant(player);
			notifyStonePlaced(place);
			if (isWonBy(player)) {
				notifyGameOver(player); // win
			} else if (isFull()) {
				notifyGameOver(null); // tie
			}
		}
	}

	/**
	 * Return true if the given player has a winning row containing the
	 * specified place in the specified direction, where a direction is
	 * represented as:
	 * <ul>
	 * <li>horizontal: dx = 1, dy = 0</li>
	 * <li>vertical: dx = 0, dy = 1</li>
	 * <li>diagonal (\): dx = 1, dy = 1</li>
	 * <li>diagonal (/): dx = 1, dy = -1</li>
	 * </ul>
	 */
	private boolean isWonBy(int x, int y, int dx, int dy, Player player) {
		// consecutive places occupied by the given player
		final List<Spot> row = new ArrayList<>(5);

		// check left/lower side of (x,y)
		int sx = x; // starting x and y
		int sy = y; // i.e., (sx, sy) <----- (x,y)
		while (!(dx > 0 && sx < 0) && !(dx < 0 && sx >= size)
				&& !(dy > 0 && sy < 0) && !(dy < 0 && sy >= size)
				&& isOccupiedBy(sx, sy, player) && row.size() < 5) {
			row.add(getSpot(sx, sy));
			sx -= dx;
			sy -= dy;
		}

		// check right/higher side of (x,y)
		int ex = x + dx; // ending x and y
		int ey = y + dy; // i.e., (x,y) -----> (ex, ey)
		while (!(dx > 0 && ex >= size) && !(dx < 0 && ex < 0)
				&& !(dy > 0 && ey >= size) && !(dy < 0 && ey < 0)
				&& isOccupiedBy(ex, ey, player) && row.size() < 5) {
			row.add(getSpot(ex, ey));
			ex += dx;
			ey += dy;
		}

		if (row.size() >= 5) {
			winningStones = row;
		}
		return row.size() >= 5;
	}

	/** Return true if the given player has a winning row. */
	public boolean isWonBy(Player player) {
		return spotLst.stream().anyMatch(p -> isWonBy(p.x, p.y, 1, 0, player) // horizontal
				|| isWonBy(p.x, p.y, 0, 1, player) // vertical
				|| isWonBy(p.x, p.y, 1, 1, player) // diagonal(\)
				|| isWonBy(p.x, p.y, 1, -1, player)); // diagonal(/)
	}

	/** Register the given listener for board changes. */
	public void addGridEventstListener(GridEventListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/** Unregister the given listener. */
	public void removeGridEventsListener(GridEventListener listener) {
		listeners.remove(listener);
	}

	/** Notify to registered listeners when a stone is placed. */
	private void notifyStonePlaced(Spot place) {
		listeners.forEach(l -> l.stonePlaced(place));
	}

	private void notifyGameOver(Player player) {
		listeners.forEach(l -> l.gameOver(player));
	}

}
