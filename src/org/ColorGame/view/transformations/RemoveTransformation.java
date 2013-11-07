package org.ColorGame.view.transformations;

import org.ColorGame.view.Cell;
import org.ColorGame.view.GameBoardView;

public class RemoveTransformation extends BoardViewTransformation {
	private final static int TIME = 150;
	
	private Cell c;
	
	private int x;
	private int y;
	
	private GameBoardView view;
	
	public RemoveTransformation(long st, GameBoardView v, int px, int py) {
		super(st, TIME);
		this.view = v;
		
		this.x = px;
		this.y = py;
	}

	@Override
	public void init() {
		c = view.findCell(x, y);
		
		if (c == null) resetToNow();
	}

	@Override
	protected void update(double perc) {
		c.setAlpha((float) (1 - perc));
	}

	@Override
	protected void finish() {
		view.forceRemoveCell(c);
	}

}
