package org.ColorGame.view.transformations;

import org.ColorGame.view.Cell;
import org.ColorGame.view.GameBoardView;
import org.newdawn.slick.geom.Point;

public class WiggleTransformation extends BoardViewTransformation {
	private final static int TIME = 400;
	
	private Cell c;
	
	private Point start;
	
	private int tx;
	private int ty;
	
	private float width;
	private float height;

	private GameBoardView view;
	
	private float shakeX;
	private float shakeY;
	
	public WiggleTransformation(long st, GameBoardView v, int nx, int ny, int ox, int oy) {
		super(st, TIME);
		
		this.view = v;
		
		tx = nx;
		ty = ny;
		
		shakeY = -(ox - nx);
		shakeX = (oy - ny);
		
		double len = Math.sqrt(shakeX*shakeX + shakeY*shakeY);
		
		shakeX /= len;
		shakeY /= len;
		
		if (shakeX < 0) {
			shakeX *= -1;
			shakeY *= -1;
		} else if (shakeX == 0) {
			shakeY = 1;
		}
	}

	@Override
	public void init() {
		c = view.findCell(tx, ty);
		
		width = c.getWidth() * 0.33f;
		height = c.getHeight() * 0.33f;
		start = new Point(c.getPosX(), c.getPosY());
		
		if (c == null) resetToNow();
	}

	@Override
	protected void update(double perc) {
		float pos = (float) (perc * 2);
		
		while(pos > 1) pos -= 1;
		
		pos = (float) Math.sin(pos * 2 * Math.PI);
		
		c.setDispPosition(start.getX() + width * pos * shakeX, start.getY() + height * pos * shakeY);
	}

	@Override
	protected void finish() {
		c.setDispPosition(start.getX(), start.getY());
	}

}
