package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import java.util.List;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;
import dk.kea.class2017.anders.gameengine.GameEngine.Sound;
import dk.kea.class2017.anders.gameengine.GameEngine.TouchEvent;

public class GameScreen extends Screen {

    enum State {
        Paused, Running, GameOver
    }

    State state = State.Running;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    Typeface font;
    Sound bounceSound;
    Sound blockSound;
    Sound gameoverSound;
    MyCollisionListener myCollisionListener;
    World world;
    WorldRenderer renderer;

    public GameScreen(GameEngine game) {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        font = game.loadFont("font.ttf");
        bounceSound = game.loadSound("bounce.wav");
        blockSound = game.loadSound("blocksplosion.wav");
        myCollisionListener = new MyCollisionListener(bounceSound, bounceSound, blockSound);
        world = new World(game, myCollisionListener);
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime) {
        if (state == State.Paused && game.isTouchDown(0)) {
            state = State.Running;
        }
        if (state == State.GameOver && game.isTouchDown(0)) {
            List<TouchEvent> events = game.getTouchEvents();
            for (TouchEvent event : events) {
                //if (event.type == TouchEvent.TouchEventType.Up) { //todo for some reason the list only contains the most recent item therefor never UP (but down)
                    game.setScreen(new MainMenuScreen(game));
                    return;
                //}
            }
        }
        if (state == State.Running && game.isTouchDown(0)
                && game.getTouchX(0) > game.getFrameBufferWidth()-40
                && game.getTouchY(0) < 40) {  // 40 is the height of the pause icon in the top right corner of our background
            pause();
        }
        game.drawBitmap(background, 0, 0);

        if (state == State.Running) {
            world.update(deltaTime, game.getAccelerometer()[0]);
        }
        game.drawText(font, "Score: " + Integer.toString(world.points), 25, 11, Color.GREEN, 12);
        renderer.render();
        if (world.gameOver) {
            state = State.GameOver;
        }


        if (state == State.Paused) {
            game.drawBitmap(resume, game.getFrameBufferWidth()/2 - resume.getWidth()/2,
                                    game.getFrameBufferHeight()*2/3 - resume.getHeight()/2);
        }
        if (state == State.GameOver) {
            game.drawBitmap(gameOver, game.getFrameBufferWidth()/2 - gameOver.getWidth()/2,
                    game.getFrameBufferHeight()*2/3 - gameOver.getHeight()/2);
        }
    }

    @Override
    public void pause() {
        if (state == State.Running) {
            state = State.Paused;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
