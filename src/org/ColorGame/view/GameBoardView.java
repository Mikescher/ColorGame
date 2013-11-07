package org.ColorGame.view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ColorGame.ColorGameState;
import org.ColorGame.logic.FieldChangeListener;
import org.ColorGame.logic.GameBoard;
import org.ColorGame.view.transformations.AddTransformation;
import org.ColorGame.view.transformations.BoardViewTransformation;
import org.ColorGame.view.transformations.MoveTransformation;
import org.ColorGame.view.transformations.RemoveTransformation;
import org.ColorGame.view.transformations.WiggleTransformation;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.FontUtils;

public class GameBoardView implements FieldChangeListener {
	private final static TrueTypeFont numberFont = new TrueTypeFont(new Font("Arial", 0, 24), true);

	private final int width;
	private final int height;
	private final int posX;
	private final int posY;
	
	private final GameBoard board;

	private List<Cell> cells;
	private Color[] colorArr;

	private List<BoardViewTransformation> actions;

	private int points = 0;
	private Point selected = new Point(-1, -1);
	
	private long transformationFreezeTime = -1;

	public GameBoardView(GameBoard b, int x, int y, int w, int h) {
		this.board = b;
		this.posX = x;
		this.posY = y;
		this.width = w;
		this.height = h;

		actions = new ArrayList<>();

		b.addListener(this);

		initColorArray();
		initCells();
	}

	private void initColorArray() {
		int len = board.getColorCount();

		colorArr = new Color[len];

		ArrayList<Double> ls_R = new ArrayList<>();
		ArrayList<Double> ls_G = new ArrayList<>();
		ArrayList<Double> ls_B = new ArrayList<>();

		for (int i = 0; i < len; i++) {
			double phi = ((2 * Math.PI) / len) * i;
			double y = 0.5;

			double u = Math.cos(phi);
			double v = Math.sin(phi);

			double r = y + v / 0.88;
			double g = y - 0.38 * u - 0.58 * v;
			double b = y + u / 0.49;

			ls_R.add(r);
			ls_G.add(g);
			ls_B.add(b);
		}

		double min_R = Collections.min(ls_R);
		double min_G = Collections.min(ls_G);
		double min_B = Collections.min(ls_B);

		double max_R = Collections.max(ls_R);
		double max_G = Collections.max(ls_G);
		double max_B = Collections.max(ls_B);

		for (int i = 0; i < len; i++) {
			float r = (float) ((ls_R.get(i) - min_R) / (max_R - min_R));
			float g = (float) ((ls_G.get(i) - min_G) / (max_G - min_G));
			float b = (float) ((ls_B.get(i) - min_B) / (max_B - min_B));

			colorArr[i] = new Color(r, g, b);
		}
	}

	private void initCells() {
		cells = new ArrayList<>();
	}
	
	public Rectangle getRectForCell(int xx, int yy) {
		float cw = (width * 0.85f) / (board.getWidth());
		float ch = (height * 0.85f) / (board.getHeight());

		float bx = (width * 0.15f) / (board.getWidth());
		float by = (height * 0.15f) / (board.getHeight());
		
		return new Rectangle(posX + bx / 2 + xx * (cw + bx), posY + by / 2 + yy * (ch + by), cw, ch);
	}

	private Color getColorForIndex(int idx, float a) {
		if (idx < 0) return Color.black;
		return new Color(colorArr[idx].r, colorArr[idx].g, colorArr[idx].b, a);
	}

	@Override
	public void onCellAdded(int x, int y, int col) {
		AddTransformation(new AddTransformation(getLastTransformationTime(), this, x, y, col));
	}

	@Override
	public void onCellRemoved(int x, int y, int col) {
		AddTransformation(new RemoveTransformation(getLastTransformationTime(), this, x, y));
	}

	@Override
	public void onCellColorMoved(int prevX, int prevY, int nextX, int nextY, int col) {
		AddTransformation(new MoveTransformation(getLastTransformationTime(), this, prevX, prevY, nextX, nextY));
	}

	@Override
	public void onCellSwitch(int px, int py, int nx, int ny) {
		long time = getLastTransformationTime();
		AddTransformation(new MoveTransformation(time, this, px, py, nx, ny));
		AddTransformation(new MoveTransformation(time, this, nx, ny, px, py));
	}

	@Override
	public void onAddPoints(int p) {
		points += p;
	}

	public void update() {
		for (int i = actions.size() - 1; i >= 0; i--) {
			if (actions.get(i).executeTop()) actions.remove(i);
		}
		
		for (int i = actions.size() - 1; i >= 0; i--) {
			if (actions.get(i).executeLow()) actions.remove(i);
		}
		
		if (! isBusy()) testSync();
	}
	
	private void testSync() {
		boolean sync = true;

		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				Cell c = findCell(x, y);
				
				if (c == null) {
					sync = false;
					continue;
				}

				sync &= c.getColor() == board.getColor(x, y);
			}
		}

		if (!sync) {
			System.err.println("OUT OF SYNC !!!!");
			sync();
		}
	}

	private boolean isBusy() {
		return !actions.isEmpty();
	}

	public void render(Graphics g) throws SlickException {
		for (Cell cell : cells) {
			if (selected.getX() == cell.getX() && selected.getY() == cell.getY()) {
				Shape c = new Rectangle(cell.getPosX() + cell.getWidth() / 4f, cell.getPosY() + cell.getHeight() / 4f, cell.getWidth() / 2f, cell.getHeight() / 2f);
				int col = cell.getColor();
				g.setColor(getColorForIndex(col, cell.getAlpha()));
				g.fill(c);
			} else {
				int col = cell.getColor();
				g.setColor(getColorForIndex(col, cell.getAlpha()));
				g.fill(cell.getShape());

				FontUtils.drawCenter(numberFont, col + "", (int) cell.getPosX(), (int) cell.getCenterPosY() - 14, (int) cell.getWidth(), Color.black);
			}
		}

		g.setColor(Color.white);
		g.setFont(numberFont);

		FontUtils.drawCenter(numberFont, "Points: " + points, 0, height, width, Color.white);
		g.drawString(String.format("%6.2f / %4.0fs", board.getTimekeeper().getElapsedTime(), board.getTimekeeper().getMaxTime()), width - 170, height);
		g.drawString(String.format("Turns: %2d / %2d", board.getTimekeeper().getTurnCount(), ColorGameState.TURN_COUNT), 5, height);
	}

	private boolean isSelected() {
		return selected.getX() >= 0 && selected.getY() >= 0;
	}

	public void mousePressed(int x, int y) {
		if (isBusy()) return;
		
		for (Cell cell : cells) {
			if (cell.contains(x, y)) {
				Point p = new Point(cell.getX(), cell.getY());
				if (isSelected()) {
					if (selected.equals(p)) {
						clearSelection();
					} else {
						if (board.canSwitch(selected, p)) {
							board.switchCells(selected, p);
							clearSelection();
						} else {
							int ax = selected.getX();
							int ay = selected.getY();
							int bx = p.getX();
							int by = p.getY();
							clearSelection();
							long time = getLastTransformationTime();
							AddTransformation(new WiggleTransformation(time, this, ax, ay, bx, by));
							AddTransformation(new WiggleTransformation(time, this, bx, by, ax, ay));
						}
					}
				} else {
					setSelection(p.getX(), p.getY());
				}
				
				return;
			}
		}
	}

	private void setSelection(int x, int y) {
		selected = new Point(x, y);
	}

	private void clearSelection() {
		selected = new Point(-1, -1);
	}

	public void resetPoints() {
		points = 0;
	}

	public int getPoints() {
		return points;
	}

	public Cell findCell(int px, int py) {
		for (Cell cell : cells) {
			if (cell.getX() == px && cell.getY() == py) {
				return cell;
			}
		}
		return null;
	}
	
	public void AddTransformation(BoardViewTransformation t) {
		actions.add(t);
	}
	
	public void freezeTransformationTime() {
		transformationFreezeTime = getLastTransformationTime();
	}
	
	public void unFreezeTransformationTime() {
		transformationFreezeTime = -1;
	}
	
	public long getLastTransformationTime() {
		if (transformationFreezeTime >= 0) return transformationFreezeTime;
		
		long t = System.currentTimeMillis();
		
		for (BoardViewTransformation bvt : actions) {
			t = Math.max(t, bvt.getEnd());
		}
		
		return t;
	}

	public void forceRemoveCell(Cell c) {
		cells.remove(c);
	}

	public Cell forceAddCell(int x, int y, int c) {
		Cell cell = new Cell(x, y, getRectForCell(x, y), c);
		cells.add(cell);
		return cell;
	}

	@Override
	public void sync() {
		actions.clear();
		cells.clear();
		
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				cells.add(new Cell(x, y, getRectForCell(x, y), board.getColor(x, y)));
			}
		}
	}

	@Override
	public void beginTransformationBlock() {
		freezeTransformationTime();
	}

	@Override
	public void endTransformationBlock() {
		unFreezeTransformationTime();
	}
}
