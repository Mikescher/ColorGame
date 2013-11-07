package org.ColorGame;

import org.ColorGame.logic.GameBoard;
import org.ColorGame.view.GameBoardView;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

public class ColorGameState extends BasicGameState {
	public final static int COLOR_COUNT  = 7;
	public final static int BOARD_WIDTH  = 9;
	public final static int BOARD_HEIGHT = 9;
	public final static int TIME_INITIAL = 120;
	public final static int TIME_BONUS   = 45;
	public final static int TURN_COUNT   = 10;
	
	public final static boolean SHOW_MAP_BUILD = false;
	
	private GameBoard board;
	private GameBoardView view;
	
    public ColorGameState() {
       
    }

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
    	reinit();
	}

	public void reinit() {
		board = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT, 7);
    	view = new GameBoardView(board, 0, 0, BOARD_WIDTH*66, BOARD_HEIGHT*66);
    	
    	board.init();
    	view.resetPoints();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.clear();
		
		view.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		view.update();
		
		if (board.getTimekeeper().isGameOver()) gameOver(game);
		
		if (container.getInput().isKeyDown(Input.KEY_U)) {
    		view.sync();
    	} else if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
    		gameOver(game);
    	}
	}

	@Override
	public int getID() {
		return App.STATE_GAME;
	}
	
    @Override
    public void mousePressed(int button, int x, int y) {
    	if (button == 0) {
    		view.mousePressed(x, y);
    	}
    }
    
    public void gameOver(StateBasedGame game) {
    	((GameOverState)game.getState(App.STATE_OVER)).setPoints(view.getPoints());
    	game.enterState(App.STATE_OVER, new EmptyTransition(), new HorizontalSplitTransition());
    }
}
