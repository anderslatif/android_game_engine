package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine;
import dk.kea.class2017.anders.gameengine.Music;
import dk.kea.class2017.anders.gameengine.Screen;

public class MainMenuScreen extends Screen {

    Bitmap background;
    Bitmap insertCoin;
    Music music;

    public MainMenuScreen(GameEngine game) {
        super(game);
        background = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
        music = game.loadMusic("music.ogg");
        music.setLooping(true);
        music.play();

    }

    @Override
    public void update(float deltaTime) {
        game.drawBitmap(background, 0, 0);
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
