package dk.kea.class2017.anders.gameengine;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

import java.security.Key;
import java.util.Random;

public class SimpleScreen extends Screen {

    int x = 0;
    int y = 0;
    Bitmap bitmap;
    int clearColor = Color.BLUE;

    public SimpleScreen(GameEngine game) {
        super(game);

        bitmap = game.loadBitmap("bob.png");
    }

    @Override
    public void update(float deltaTime) {

        game.clearFrameBuffer(clearColor);

        for (int pointer=0; pointer<5; pointer++) {
            if (game.isTouchDown(pointer)) {
                game.drawBitmap(bitmap, game.getTouchX(pointer), game.getTouchY(pointer));
            }
        }

        float x = -game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        float accConstant = 10;
        x = (x/accConstant) * game.getFrameBufferWidth()/2 + game.getFrameBufferWidth()/2;
        y = (y/accConstant) * game.getFrameBufferHeight()/2 + game.getFrameBufferHeight()/2;

        game.drawBitmap(bitmap, (int)(x-(float)bitmap.getWidth()/2), (int)(y-(float)bitmap.getHeight()/2));

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
