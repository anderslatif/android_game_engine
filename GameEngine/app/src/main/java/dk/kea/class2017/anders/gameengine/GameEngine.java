package dk.kea.class2017.anders.gameengine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable {

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas = null;


    public abstract Screen createStartScreen();

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Setting the bits in our status byte, each bit is like a bool
        // we can set the bit with OR and we can get the status by AND'ing with the mask and check if result is bigger than zero
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // a surface view has direct access to the screen without delay  .. unlike other views where the hardware schedules for better performance
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
        InputStream in = null;
        Bitmap bitmap = null;

        try {
            getAssets();
            in = getAssets().open(fileName);
            // is a service class where we can call util methods statically without creating a new object
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) {
                // in case a legal file has been found but it wasn't a graphic, so the bitmap is null
                throw new RuntimeException("Could not create a bitmap from file " + fileName);
            }
            return bitmap;
        } catch(IOException e) {
            throw new RuntimeException("Could not load the file with file name: " + fileName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("GameEngine", "loadBitmap() failed to close the file " + fileName);
                }
            }
        }
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
        canvas.drawColor(color);
    }

    public int getFrameBufferWidth() {
        return surfaceView.getWidth();
    }

    public int getFrameBufferHeight() {
        return surfaceView.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y) {
        if (canvas != null) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

    Rect src = new Rect();
    Rect dst = new Rect();
    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        if (canvas == null) {
            return;
        }
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
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
                        if (screen != null) {
                            screen.dispose();
                        }
                        Log.d("GameEngine", "state changed to Disposed");
                        return;
                    }
                    if (state == State.Paused) {
                        if (screen != null) {
                            screen.pause();
                        }
                        Log.d("GameEngine", "state changed to Paused");
                        return;
                    }
                    if (state == State.Resumed) {
                        if (screen != null) {
                            screen.resume();
                        }
                        state = State.Running;
                        Log.d("GameEngine", "state changed to Resumed");
                    }
                }
                stateChanges.clear();
                if (state == State.Running) {
                    if (!surfaceHolder.getSurface().isValid()) continue;
                    canvas = surfaceHolder.lockCanvas();
                    // we will do all the drawing here

                    if (screen != null) {
                        screen.update(0);
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    // releases the canvas for garbage collection cause lockCanvas creates a new .. returning memory
                    canvas = null;
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
