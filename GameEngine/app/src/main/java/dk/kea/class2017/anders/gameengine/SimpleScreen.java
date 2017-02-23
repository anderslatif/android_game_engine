package dk.kea.class2017.anders.gameengine;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Random;

public class SimpleScreen extends Screen {

    int x = 0;
    int y = 0;
    Bitmap bitmap;
    Random random = new Random();

    public SimpleScreen(GameEngine game) {
        super(game);

        bitmap = game.loadBitmap("bob.png");
    }

    @Override
    public void update(float deltaTime) {



/*
        if (game.isTouchDown(0)) {
            x = game.getTouchX(0);
            y = game.getTouchY(0);
        }*/
        game.clearFrameBuffer(Color.BLUE);
        game.drawBitmap(bitmap, 10, 10);
        game.drawBitmap(bitmap, 100, 150, 0, 0, 64, 64);

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
