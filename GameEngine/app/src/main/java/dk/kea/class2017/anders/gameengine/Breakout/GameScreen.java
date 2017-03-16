package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class GameScreen extends Screen {

    enum GameState {
        Paused, Running, GameOver
    }

    GameState gameState = GameState.Running;
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
        if (gameState == GameState.Paused && game.isTouchDown(0)) {
            gameState = GameState.Running;
        }
        if (gameState == GameState.GameOver && game.isTouchDown(0)) {
            game.setScreen(new MainMenuScreen(game));
            return;
        }
        if (gameState == GameState.Running && game.isTouchDown(0)
                && game.getTouchX(0) > 320-40 && game.getTouchY(0) < 40) {
            pause();
        }
        game.drawBitmap(background, 0, 0);

        if (gameState == GameState.Running) {
            world.update(deltaTime);
        }
        renderer.render();


        if (gameState == GameState.Paused) {
            game.drawBitmap(resume, game.getFrameBufferWidth()/2 - resume.getWidth()/2,
                                    game.getFrameBufferHeight()/2 - resume.getHeight()/2);
        }
        if (gameState == GameState.GameOver) {
            game.drawBitmap(gameOver, game.getFrameBufferWidth()/2 - gameOver.getWidth()/2,
                    game.getFrameBufferHeight()/2 - gameOver.getHeight()/2);
        }
    }

    @Override
    public void pause() {
        if (gameState == GameState.Running) {
            gameState = GameState.Paused;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
