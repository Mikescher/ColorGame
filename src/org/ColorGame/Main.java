package org.ColorGame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new App());
            app.setDisplayMode(ColorGameState.BOARD_WIDTH*66, ColorGameState.BOARD_HEIGHT*66 + 40, false);
            app.setShowFPS(false);
            app.setAlwaysRender(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
