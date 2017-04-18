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
    float renderTimes;

    boolean gameOver = false;

    ScrollingBackground scrollingBackground;
    Car car;
    Player player;
    List<Monster> monsters = new ArrayList<>();

    public World(GameEngine game) {
        this.game = game;
        scrollingBackground = new ScrollingBackground();
        car = new Car();
        player = new Player(30, 160 - Player.HEIGHT/2);
        offScreenSurfaceWidth = game.getFrameBufferWidth();
    }

    public void update(float deltaTime) {
        scrollingBackground.scrollx = scrollingBackground.scrollx + 200 * deltaTime;
        if (scrollingBackground.scrollx > MAX_X - offScreenSurfaceWidth) {
            scrollingBackground.scrollx = 0;
        }

        renderTimes = renderTimes + 100 * deltaTime;
        if (renderTimes > 20) {
            player.spritex = player.spritex + Player.WIDTH;
            if (player.spritex == 7 * Player.WIDTH) {
                player.spritex = 0;
                player.spritey = player.spritey + Player.HEIGHT;
                if (player.spritey == 3 * player.HEIGHT) {
                    player.spritey = 0;
                }
            }
            renderTimes = 0;
        }


    }

}
