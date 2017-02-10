package dk.kea.class2017.anders.gameengine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable {

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;


    public abstract Screen createStartScreen();

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Setting the bits in our status byte, each bit is like a bool
        // we can set the bit with OR and we can get the status by AND'ing with the mask and check if result is bigger than zero
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        screen = createStartScreen();
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.dispose();
        }
        this.screen = screen;
    }

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


    public void run() {
        while (true) {
            synchronized (stateChanges) {
                for (int i=0; i<stateChanges.size(); i++) {
                    state = stateChanges.get(i);
                    if (state == State.Disposed) {
                        Log.d("GameEngine", "state changed to Disposed");
                        return;
                    }
                    if (state == State.Paused) {
                        Log.d("GameEngine", "state changed to Paused");
                        return;
                    }
                    if (state == State.Resumed) {
                        state = State.Running;
                        Log.d("GameEngine", "state changed to Resumed");
                    }
//                    stateChanges.clear();
                    if (state == State.Running) {
                        if (!surfaceHolder.getSurface().isValid()) continue;
                        Canvas canvas = surfaceHolder.lockCanvas();
                        // we will do all the drawing here
                        canvas.drawColor(Color.RED);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        synchronized (stateChanges) {
            if (isFinishing()) {
                stateChanges.add(State.Disposed);
            } else {
                stateChanges.add(State.Paused);
            }
        }

    }

    public void onResume() {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges) {
            stateChanges.add(State.Resumed);
        }
    }

}
