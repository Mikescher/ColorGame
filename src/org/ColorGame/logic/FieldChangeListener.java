package org.ColorGame.logic;

public interface FieldChangeListener {
	public void onCellRemoved(int x, int y, int col);
	public void onCellAdded(int x, int y, int col);
	public void onCellSwitch(int px, int py, int nx, int ny);
	public void onCellColorMoved(int prevX, int prevY, int nextX, int nextY, int col);
	
	public void onAddPoints(int p);
	public void beginTransformationBlock();
	public void endTransformationBlock();
	public void sync();
}
