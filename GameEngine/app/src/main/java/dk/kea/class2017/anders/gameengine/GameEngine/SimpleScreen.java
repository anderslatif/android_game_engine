package dk.kea.class2017.anders.gameengine.GameEngine;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import java.util.Random;

public class SimpleScreen extends Screen {

    float x = 0;
    float y = 0;
    Bitmap bitmap;
    int clearColor = Color.BLUE;
    Sound sound;
    Music music;
    boolean isPlaying = false;
    Vibrator vibrator;

    public SimpleScreen(GameEngine game) {
        super(game);
        bitmap = game.loadBitmap("breakout/bob.png");
        sound = game.loadSound("breakout/blocksplosion.wav");
        music = game.loadMusic("breakout/music.ogg");
        music.setLooping(true);
        music.play();
        isPlaying = true;
        vibrator = (Vibrator) game.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void update(float deltaTime) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        /*game.clearFrameBuffer(color);*/
        game.clearFrameBuffer(clearColor);

        Log.d("SimpleScreen", "********************** fps: " + game.getFrameRate());

        for (int pointer=0; pointer<5; pointer++) {
            if (game.isTouchDown(pointer)) {
                game.drawBitmap(bitmap, game.getTouchX(pointer), game.getTouchY(pointer));
                //sound.play(1);
                //vibrator.vibrate(200);
                if (music.isPlaying()) {
                    music.pause();
                    isPlaying = false;
                } else {
                    music.play();
                    isPlaying = true;
                }
            }
        }

/*        float x = -game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        float accConstant = 10;
        x = (x/accConstant) * game.getFrameBufferWidth()/2 + game.getFrameBufferWidth()/2;
        y = (y/accConstant) * game.getFrameBufferHeight()/2 + game.getFrameBufferHeight()/2;

        game.drawBitmap(bitmap, (int)(x-(float)bitmap.getWidth()/2), (int)(y-(float)bitmap.getHeight()/2));*/

        x = x + 50 * deltaTime;
        y = y + 4;
        if (x > game.getFrameBufferWidth()) {
            x = 0;
        }
        if (y > game.getFrameBufferHeight()) {
            y = 0;
        }
        game.drawBitmap(bitmap, (int)x, (int)y);
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        if (!isPlaying) {
            music.play();
        }
    }

    @Override
    public void dispose() {
        music.dispose();
    }
}
