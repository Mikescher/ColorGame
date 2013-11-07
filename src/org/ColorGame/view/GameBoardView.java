package org.ColorGame.view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;

import org.ColorGame.logic.FieldChangeListener;
import org.ColorGame.logic.GameBoard;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.FontUtils;

public class GameBoardView implements FieldChangeListener{

	private final TrueTypeFont numberFont = new TrueTypeFont(new Font("Arial", 0, 24), true);
	
	private final int width;
	private final int height;
	private final int posX;
	private final int posY;
	private final GameBoard board;
	
	private Shape[][] cells;
	private Color[] colorArr;
	private int points = 0;
	
	private Point selected = new Point(-1, -1);
	
	public GameBoardView(GameBoard b, int x, int y, int w, int h) {
		this.board = b;
		this.posX = x;
		this.posY = y;
		this.width = w;
		this.height = h;
		
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
			double phi = ((2*Math.PI) / len) * i;
			double y = 0.5;
			
			double u = Math.cos(phi);
			double v = Math.sin(phi);
			
			double r = y + v/0.88;
			double g = y - 0.38*u - 0.58*v;
			double b = y + u/0.49;
			
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
		cells = new Shape[board.getWidth()][board.getHeight()];
		
		float cw = (width*0.85f) / (board.getWidth());
		float ch = (height*0.85f) / (board.getHeight());
		
		float bx = (width*0.15f) / (board.getWidth());
		float by = (height*0.15f) / (board.getHeight());
		
		for (int xx = 0; xx < board.getWidth(); xx++) {
			for (int yy = 0; yy < board.getHeight(); yy++) {
				cells[xx][yy] = new Rectangle(posX + bx/2 + xx*(cw+bx), posY + by/2 + yy*(ch+by), cw, ch);
			}
		}
	}
	
	private Color getColorForIndex(int idx) {
		return colorArr[idx];
	}

	@Override
	public void onCellAdded(int x, int y, int col) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCellRemoved(int x, int y, int col) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCellColorMoved(int prevX, int prevY, int nextX, int nextY, int col) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onCellSwitch(int px, int py, int nx, int ny) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void onAddPoints(int p) {
		points += p;
	}

	public void render(Graphics g) throws SlickException {
		for (int xx = 0; xx < cells.length; xx++) {
			for (int yy = 0; yy < cells[0].length; yy++) {
				if (selected.getX() == xx && selected.getY() == yy) {
					Shape c = new Rectangle(cells[xx][yy].getX() + cells[xx][yy].getWidth()/4f, cells[xx][yy].getY() + cells[xx][yy].getHeight()/4f, cells[xx][yy].getWidth()/2f, cells[xx][yy].getHeight()/2f);
					int col = board.getColor(xx, yy);
					g.setColor(getColorForIndex(col));
					g.fill(c);
				} else {
					int col = board.getColor(xx, yy);
					g.setColor(getColorForIndex(col));
					g.fill(cells[xx][yy]);

					FontUtils.drawCenter(numberFont, col+"", (int)cells[xx][yy].getX(), (int)cells[xx][yy].getCenterY() - 14, (int) cells[xx][yy].getWidth(), Color.black);
				}
			}
		}
		
		FontUtils.drawCenter(numberFont, "Points: " + points, 0, 600, 600, Color.white);
	}

	private boolean isSelected() {
		return selected.getX() >= 0 && selected.getY() >= 0;
	}
	
	public void mousePressed(int x, int y) {
		for (int xx = 0; xx < cells.length; xx++) {
			for (int yy = 0; yy < cells[0].length; yy++) {
				if (cells[xx][yy].contains(x, y)) {
					Point p = new Point(xx, yy);
					if (isSelected()) {
						if (selected.equals(p)) {
							clearSelection();
						} else {
							if (board.canSwitch(selected, p)) {
								board.switchCells(selected, p);
							}
							clearSelection();
						}
					} else {
						setSelection(xx, yy);
					}
				}
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
}
