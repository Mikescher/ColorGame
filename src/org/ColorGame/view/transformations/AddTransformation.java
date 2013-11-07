package org.ColorGame.view.transformations;

import org.ColorGame.view.Cell;
import org.ColorGame.view.GameBoardView;

public class AddTransformation extends BoardViewTransformation {
	private final static int TIME = 150;
	
	private int x, y, c;
	
	private float cellh;
	private float starty;
	
	private Cell cl;
	
	private GameBoardView view;
	
	public AddTransformation(long st, GameBoardView v, int px, int py, int col) {
		super(st, TIME);
		this.view = v;
		
		this.x = px;
		this.y = py;
		this.c = col;
	}

	@Override
	public void init() {
		cl = view.findCell(x, y);
		
		if (cl != null) resetToNow();
		
		cl = view.forceAddCell(x, y, c);
		
		cellh = cl.getHeight() * 1.5f;
		starty = cl.getPosY();
		cl.setDispPosition(cl.getPosX(), starty - cellh);
	}

	@Override
	protected void update(double perc) {
		cl.setDispPosition(cl.getPosX(), (float) (starty - cellh * (1-perc)));
	}

	@Override
	protected void finish() {
		cl.setDispPosition(cl.getPosX(), starty);
	}

}
