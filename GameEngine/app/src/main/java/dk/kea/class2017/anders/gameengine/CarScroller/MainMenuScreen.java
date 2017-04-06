package dk.kea.class2017.anders.gameengine.CarScroller;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class MainMenuScreen extends Screen {


    Bitmap background;
    Bitmap startgame;
    float passedTime;



    public MainMenuScreen(GameEngine game) {
        super(game);
        background = game.loadBitmap("carscroller/carbackground.png");
        startgame = game.loadBitmap("carscroller/startgame.png");
    }

    @Override
    public void update(float deltaTime) {
        if (game.isTouchDown(0)) {
            game.music.play();
            game.setScreen(new GameScreen(game));
            return;
        }

        game.drawBitmap(background, 0, 0);

        int insertCoinWidth = game.getFrameBufferWidth() / 2 - startgame.getWidth()/2;
        int insertCoinHeight = game.getFrameBufferHeight() / 3 * 2;
        passedTime += deltaTime;
        if ((passedTime - (int)passedTime) > 0.5f) {
            game.drawBitmap(startgame, insertCoinWidth, insertCoinHeight);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
