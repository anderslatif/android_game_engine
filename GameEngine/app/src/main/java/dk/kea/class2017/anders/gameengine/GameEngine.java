package dk.kea.class2017.anders.gameengine;

import android.app.Activity;
import android.graphics.Bitmap;

public abstract class GameEngine extends Activity {

    public abstract Screen createStartScreen();

    public void setScreen(Screen screen) {}

    public Bitmap loadBitmap(String fileName) {
        return null;
    }

/*
    public Music loadMusic(String fileName) {
        return null;
    }

    public Sound loadSound(String fileName) {
        return null;
    }
*/

    public void clearFrameBuffer(int color) {

    }

    public int getFrameBufferWidth() {
        return 0;
    }

    public int getFrameBufferHeight() {
        return 0;
    }

    public void drawBitmap(Bitmap bitmap, int x, int y) {

    }

    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {

    }

    public boolean isKeyPressed(int keyCode) {
        return false;
    }

    public boolean isTouchDown(int pointer) {
        return false;
    }

    public int getTouchX(int pointer) {
        return 0;
    }

    public int getTouchY(int pointer) {
        return 0;
    }

/*    public List<com.badlogic.agd.KeyEvent> getKeyEvents() {
        return null;
    }

    public List<com.badlogic.agd.KeyEvent> getKeyEvents() {
        return null;
    }*/

/*    public abstract float[] getAccelerometer();*/

}
