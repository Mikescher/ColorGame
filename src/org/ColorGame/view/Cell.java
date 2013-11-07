package org.ColorGame.view;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Cell {
	private Shape shape;
	
	// X && Y On Grid
	private int x;
	private int y;
	
	private int color;
	
	private float alpha = 1;
	
	public Cell(int x, int y, Rectangle r, int col) {
		this.x = x;
		this.y = y;
		this.color = col;
		this.shape = new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	public float getPosY() {
		return shape.getY();
	}

	public float getPosX() {
		return shape.getX();
	}
	
	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public float getWidth() {
		return shape.getWidth();
	}

	public float getHeight() {
		return shape.getHeight();
	}

	public float getCenterPosY() {
		return shape.getCenterY();
	}

	public Shape getShape() {
		return shape;
	}

	public boolean contains(int x, int y) {
		return shape.contains(x, y);
	}

	public void setPosition(int tx, int ty) {
		x = tx;
		y = ty;
	}

	public void setDispPosition(float xx, float yy) {
		shape.setX(xx);
		shape.setY(yy);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
}
