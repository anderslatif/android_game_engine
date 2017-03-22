package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class GameScreen extends Screen {

    enum State {
        Paused, Running, GameOver
    }

    State state = State.Running;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    World world;
    WorldRenderer renderer;

    public GameScreen(GameEngine game) {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World();
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime) {
        if (state == State.Paused && game.isTouchDown(0)) {
            state = State.Running;
        }
        if (state == State.GameOver && game.isTouchDown(0)) {
            game.setScreen(new MainMenuScreen(game));
            return;
        }
        if (state == State.Running && game.isTouchDown(0)
                && game.getTouchX(0) > game.getFrameBufferWidth()-resume.getWidth()
                && game.getTouchY(0) < resume.getWidth()) {
            pause();
        }
        game.drawBitmap(background, 0, 0);

        if (state == State.Running) {
            world.update(deltaTime, game.getAccelerometer()[0]);
            renderer.render();
        }
        if (world.gameOver) {
            state = State.GameOver;
        }


        if (state == State.Paused) {
            game.drawBitmap(resume, game.getFrameBufferWidth()/2 - resume.getWidth()/2,
                                    game.getFrameBufferHeight()/2 - resume.getHeight()/2);
        }
        if (state == State.GameOver) {
            game.drawBitmap(gameOver, game.getFrameBufferWidth()/2 - gameOver.getWidth()/2,
                    game.getFrameBufferHeight()/2 - gameOver.getHeight()/2);
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
