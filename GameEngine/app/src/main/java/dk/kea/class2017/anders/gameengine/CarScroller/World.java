package dk.kea.class2017.anders.gameengine.CarScroller;


import java.util.ArrayList;
import java.util.List;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;

public class World {

    public static final float MIN_X = 0;
    public static final float MAX_X = 2699;
    public static final float MIN_Y = 30;  // the logical playing field
    public static final float MAX_Y = 289; // do not allow monsters to appear or car to go on the "grass" (edges of the screen)

    private GameEngine game;
    int offScreenSurfaceWidth;

    boolean gameOver = false;

    ScrollingBackground scrollingBackground;
    Car car;
    List<Monster> monsters = new ArrayList<>();

    public World(GameEngine game) {
        this.game = game;
        scrollingBackground = new ScrollingBackground();
        car = new Car();
        offScreenSurfaceWidth = game.getFrameBufferWidth();
    }

    public void update(float deltaTime) {
        scrollingBackground.scrollx = scrollingBackground.scrollx + 200 * deltaTime;
        if (scrollingBackground.scrollx > MAX_X - offScreenSurfaceWidth) {
            scrollingBackground.scrollx = 0;
        }

    }

}
