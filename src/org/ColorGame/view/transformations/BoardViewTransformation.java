package org.ColorGame.view.transformations;

public abstract class BoardViewTransformation {
	protected long startTime;
	protected long duration;
	
	protected boolean started = false;
	protected boolean finished = false;
	
	private int resetCount = 0;
	
	public BoardViewTransformation(long st, int dur) {
		startTime = st;
		duration = dur;
	}
	
	protected long now() {
		return System.currentTimeMillis();
	}
	
	protected boolean hasStarted() {
		return now() > startTime;
	}
	
	protected double getPerc() {
		return (now() - startTime)/(duration * 1.0);
	}
	
	protected boolean hasFinished() {
		if (getPerc() >= 1) {
			if (! finished) {
				finish();
				finished = true;
			}
			return true;
		} else {
			return false;
		}
	}
	
	protected void update() {
		if (! started) {
			started = true;
			init();
			if (started == false) return;
		}
		
		update(getPerc());
	}
	
	public boolean executeTop() {
		if (getPerc() < 0.5) return false;
		
		if (hasStarted()) {
			update();
			
			if (hasFinished()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean executeLow() {
		if (getPerc() > 0.5) return false;
		
		if (hasStarted()) {
			update();
			
			if (hasFinished()) {
				return true;
			}
		}
		
		return false;
	}
	
	public long getEnd() {
		return startTime + duration;
	}
	
	protected void resetToNow() {
		resetCount++;
		
		started = false;
		finished = false;
		
		startTime = now();
		
		if (resetCount > 5000) {
			started = true;
			finished = true;
			startTime = 0;
			System.err.println("FORCE Reset Quit");
		}
	}
	
	public abstract void init();
	protected abstract void update(double perc);
	protected abstract void finish();
}
