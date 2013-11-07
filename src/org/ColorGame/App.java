package org.ColorGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class App extends StateBasedGame {
	public final static int STATE_GAME  = 0;
	public final static int STATE_OVER  = 1;
	public final static int STATE_START = 2;
	
    public App() {
        super("ColorGame");  
    }

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		addState(new ColorGameState());
		addState(new GameOverState());
	}
}