package org.ColorGame.logic;

import java.util.ArrayList;
import java.util.Random;

import org.ColorGame.ColorGameState;
import org.lwjgl.util.Point;

public class GameBoard {
	private final static int COLOR_NOCOL = -1;
	private final static int MIN_COLL_WIDTH = 3;

	private Random rand = new Random();
	
	private ArrayList<FieldChangeListener> listener = new ArrayList<>();
	
	private final int width;
	private final int height;
	private final int colorCount;

	private int[][] board;
	private TimeKeeper timekeeper = new TimeKeeper();
	
	private boolean suppressListener = false;

	public GameBoard(int w, int h, int colCount) {
		this.width = w;
		this.height = h;
		this.colorCount = colCount;

		board = new int[w][h];
	}
	
	public void init() {
		suppressListener = true;
		clearBoard();
		suppressListener = false;
		
		updateEmptyBoardCells();
		
		timekeeper.restart();
		
		if (! ColorGameState.SHOW_MAP_BUILD)
			if (! suppressListener) for (FieldChangeListener f : listener) f.sync();
	}

	private void clearBoard() {
		clearArea(0, 0, width, height);
	}
	
	public void switchCells(Point a, Point b) {
		switchCells(a.getX(),  a.getY(), b.getX(), b.getY());
	}
	
	public void switchCells(int ax, int ay, int bx, int by) {
		switchColor(ax, ay, bx, by);
		
		timekeeper.doTurn();
		
		doCollisions();
	}

	private void updateGravitation() {
		boolean changed = true;

		while (changed) {
			if (! suppressListener) for (FieldChangeListener f : listener) f.beginTransformationBlock();
			
			changed = false;
			for (int y = (height - 1); y > 0; y--) {
				for (int x = 0; x < width; x++) {
					if (getColor(x, y) == COLOR_NOCOL && getColor(x, y-1) != COLOR_NOCOL) {
						moveColor(x, y - 1, x, y);
						changed = true;
					}
				}
			}
			
			if (! suppressListener) for (FieldChangeListener f : listener) f.endTransformationBlock();
		}
	}

	private void updateEmptyBoardCells() {
		boolean changed = true;

		while (changed) {
			changed = false;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < 1; y++) {
					if (getColor(x, y) == COLOR_NOCOL) {
						setColor(x, y, getRandomColor());
						changed = true;
					}
				}
			}
			updateGravitation();
		}

		doCollisions();
	}

	private int getRandomColor() {
		return rand.nextInt(colorCount);
	}

	private void doCollisions() {
		int collcounter = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (getColor(x, y) != COLOR_NOCOL) {
					int colX = getCollisionSize(x, y, 1, 0);
					int colY = getCollisionSize(x, y, 0, 1);

					if (colX >= MIN_COLL_WIDTH && colY >= MIN_COLL_WIDTH) {
						collcounter += colX + colY - 1;
						clearArea(x, y, colX, 1);
						clearArea(x, y, 1, colY);
					} else if (colX >= MIN_COLL_WIDTH) {
						collcounter += colX;
						clearArea(x, y, colX, 1);
					} else if (colY >= MIN_COLL_WIDTH) {
						collcounter += colY;
						clearArea(x, y, 1, colY);
					}
				}
			}
		}
		
		if (collcounter > 0) {
			for (FieldChangeListener f : listener) f.onAddPoints(collcounter);
			
			updateGravitation();
			updateEmptyBoardCells();
		}
	}
	
	private int getCollisionCount() {
		int collcounter = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (getColor(x, y) != COLOR_NOCOL) {
					int colX = getCollisionSize(x, y, 1, 0);
					int colY = getCollisionSize(x, y, 0, 1);

					if (colX >= MIN_COLL_WIDTH && colY >= MIN_COLL_WIDTH) {
						collcounter += colX + colY - 1;
					} else if (colX >= MIN_COLL_WIDTH) {
						collcounter += colX;
					} else if (colY >= MIN_COLL_WIDTH) {
						collcounter += colY;
					}
				}
			}
		}
		
		return collcounter;
	}

	private int getCollisionSize(int x, int y, int dx, int dy) {
		int w = 0;
		int col = getColor(x, y);

		while (x < width && y < height) {
			if (getColor(x, y) == col) {
				w++;
				x += dx;
				y += dy;
			} else break;
		}

		return w;
	}
	
	private void clearArea(int x, int y, int w, int h) {
		int l = Math.min(width,  x + w);
		int t = Math.min(height, y + h); 
		
		for (int xx = x; xx < l; xx++) {
			for (int yy = y; yy < t; yy++) {
				clearColor(xx, yy);
			}
		}
	}

	public int getColor(int x, int y) {
		return board[x][y];
	}

	public void setColor(int x, int y, int c) {
		if (getColor(x, y) != COLOR_NOCOL) System.err.println("Tryd to change field col: " + x + "::" + y + "::" + c);
		board[x][y] = c;
		
		if (! suppressListener) for (FieldChangeListener f : listener) f.onCellAdded(x, y, c);
	}
	
	public void moveColor(int px, int py, int nx, int ny) {
		int c = getColor(px, py);
		if (c == COLOR_NOCOL) System.err.println("Tryd to change field col: " + px + "::" + py + "::" + c);
		board[nx][ny] = c;
		board[px][py] = COLOR_NOCOL;
		
		if (! suppressListener) for (FieldChangeListener f : listener) f.onCellColorMoved(px, py, nx, ny, c);
	}
	
	public void clearColor(int x, int y) {
		int c = getColor(x, y);
		board[x][y] = COLOR_NOCOL;
		
		if (! suppressListener) for (FieldChangeListener f : listener) f.onCellRemoved(x, y, c);
	}
	
	private void switchColor(int ax, int ay, int bx, int by) {
		int a = getColor(ax, ay);
		int b = getColor(bx, by);
		
		board[ax][ay] = b;
		board[bx][by] = a;
		
		if (! suppressListener) for (FieldChangeListener f : listener) f.onCellSwitch(ax, ay, bx, by);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void addListener(FieldChangeListener l) {
		listener.add(l);
	}

	public int getColorCount() {
		return colorCount;
	}

	public boolean canSwitch(Point a, Point b) {
		return canSwitch(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	public boolean canSwitch(int ax, int ay, int bx, int by) {
		int dist = Math.abs(ax - bx) + Math.abs(ay - by);
		
		if (dist != 1) return false;
		
		int a = getColor(ax, ay);
		int b = getColor(bx, by);
		
		// SWITCH
		board[ax][ay] = b;
		board[bx][by] = a;
		
		int collC = getCollisionCount();
		
		// UN - SWITCH
		board[ax][ay] = a;
		board[bx][by] = b;
		
		return collC > 0;
	}

	public TimeKeeper getTimekeeper() {
		return timekeeper;
	}
}
