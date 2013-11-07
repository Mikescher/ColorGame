package org.ColorGame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(600, 640, false);
            app.setShowFPS(false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
