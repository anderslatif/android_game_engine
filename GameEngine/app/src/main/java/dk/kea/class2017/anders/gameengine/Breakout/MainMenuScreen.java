package dk.kea.class2017.anders.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Music;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class MainMenuScreen extends Screen {

    Bitmap mainMenuBackground;
    Bitmap insertCoin;
    float passedTime = 0;

    public MainMenuScreen(GameEngine game) {
        super(game);
        mainMenuBackground = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
        game.music.pause();
    }

    @Override
    public void update(float deltaTime) {

        if (game.isTouchDown(0)) {
            game.music.play();
            game.setScreen(new GameScreen(game));
            return;
        }

        game.drawBitmap(mainMenuBackground, 0, 0);

        int insertCoinWidth = game.getFrameBufferWidth() / 2 - insertCoin.getWidth()/2;
        int insertCoinHeight = game.getFrameBufferHeight() / 3 * 2;
        passedTime += deltaTime;
        if ((passedTime - (int)passedTime) > 0.5f) {
            game.drawBitmap(insertCoin, insertCoinWidth, insertCoinHeight);
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
