package org.ColorGame;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

public class MainMenuState extends BasicGameState {
	private final UnicodeFont mainFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 20), 256, true, false);
	
	private boolean clicked = false;
	
	@SuppressWarnings("unchecked")
	public MainMenuState() {
		// INIT FONT
		mainFont.getEffects().add(new ColorEffect(Color.WHITE));
		
		mainFont.addAsciiGlyphs();
		try {
			mainFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		//
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		FontUtils.drawCenter(mainFont, "GO", 0, ColorGameState.BOARD_HEIGHT * 66 / 2 - 128, ColorGameState.BOARD_WIDTH * 66);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (clicked) {
			clicked = false;
			
			game.enterState(App.STATE_GAME);
		}
	}
	
    @Override
    public void mousePressed(int button, int x, int y) {
    	clicked = true;
    }

	@Override
	public int getID() {
		return App.STATE_START;
	}
}
