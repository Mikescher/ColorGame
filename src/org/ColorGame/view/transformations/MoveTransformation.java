package org.ColorGame.view.transformations;

import org.ColorGame.view.Cell;
import org.ColorGame.view.GameBoardView;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class MoveTransformation extends BoardViewTransformation {
	private final static int TIME = 300;
	
	private Cell c;
	
	private Point start;
	private Point dist;
	
	private int tx;
	private int ty;
	
	private int prevX;
	private int prevY;
	private GameBoardView view;
	
	public MoveTransformation(long st, GameBoardView v, int px, int py, int nx, int ny) {
		super(st, TIME);
		
		this.tx = nx;
		this.ty = ny;
		
		this.prevX = px;
		this.prevY = py;
		
		this.view = v;
		
		Rectangle r1 = view.getRectForCell(px, py);
		Rectangle r2 = view.getRectForCell(nx, ny);
		
		start = new Point(r1.getX(), r1.getY());
		dist = new Point(r2.getX() - r1.getX(), r2.getY() - r1.getY());
	}

	@Override
	public void init() {
		c = view.findCell(prevX, prevY);
		
		if (c == null) resetToNow();
	}

	@Override
	protected void update(double perc) {
		c.setDispPosition(start.getX() + dist.getX() * (float)perc, start.getY() + dist.getY() * (float)perc);
	}

	@Override
	protected void finish() {
		c.setDispPosition(start.getX() + dist.getX(), start.getY() + dist.getY());
		c.setPosition(tx, ty);
	}

}
