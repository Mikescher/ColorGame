package org.ColorGame;

import java.awt.event.KeyEvent;

import org.ColorGame.logic.GameBoard;
import org.ColorGame.view.GameBoardView;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame
{
	private GameBoard board;
	private GameBoardView view;
	
    public Game() {
        super("ColorGame");  
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    	board = new GameBoard(9, 9, 7);
    	view = new GameBoardView(board, 0, 0, 600, 600);
    	
    	board.init();
    	view.resetPoints();
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    	if (container.getInput().isKeyDown(KeyEvent.VK_U)) {
    		// SYNC STATES
    	}
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
    	if (button == 0) {
    		view.mousePressed(x, y);
    	}
    }

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		view.render(g);
	}
}