package dk.kea.class2017.anders.gameengine.GameEngine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
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

public abstract class GameEngine extends Activity implements Runnable, SensorEventListener {

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas = null;
    private Bitmap offscreenSurface;
    Rect src = new Rect();
    Rect dst = new Rect();
    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventBufferCopied = new ArrayList<>();
    private float[] accelerometer = new float[3];
    private SoundPool soundPool;
    private int framesPerSecond = 0;
    private Paint paint = new Paint();
    public Music music;


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

        // moved to onResume()
        fixTheScreen();

        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor accSensor =  manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
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

    public Sound loadSound(String fileName) {

        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new Sound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Could not load sound file: " + fileName + "************");
        }
    }

    public Music loadMusic(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Could not load music file: " + fileName + "********************");
        }
    }

    public void clearFrameBuffer(int color) {
        canvas.drawColor(color);
    }

    public void fixTheScreen() {
        if (surfaceView.getWidth() > surfaceView.getHeight()) {
            setOffscreenSurface(480, 320);
        } else {
            setOffscreenSurface(320, 480);
        }
    }

    public void setOffscreenSurface(int width, int height) {
        if (offscreenSurface != null) {
            offscreenSurface.recycle();
        }
        offscreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface);
    }

    public int getFrameBufferWidth() {
        return offscreenSurface.getWidth();
    }

    public int getFrameBufferHeight() {
        return offscreenSurface.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y) {
        if (canvas != null) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

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

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public List<TouchEvent> getTouchEvents() {
        return touchEventBufferCopied;
    }

    public int getTouchX(int pointer) {
        return (int) (touchHandler.getTouchX(pointer) * (float) offscreenSurface.getWidth() / (float) surfaceView.getWidth());
    }

    public int getTouchY(int pointer) {
        return (int) (touchHandler.getTouchY(pointer) * (float) offscreenSurface.getHeight() / (float) surfaceView.getHeight());
    }

    private void fillEvents() {
        synchronized (touchEventBuffer) {
            // in order for other classes to get the touch events and work with them
            // we copy them from the original list and clear them and hand over the copied list
            // this way we minimize the time that the list is being locked under synchronized
            for (TouchEvent touchEvent : touchEventBuffer) {
                touchEventBufferCopied.add(touchEvent);
            }
            touchEventBuffer.clear();
        }
    }

    private void freeEvents() {
        synchronized (touchEventBufferCopied) {
            for (TouchEvent touchEventCopied : touchEventBufferCopied) {
                touchEventPool.free(touchEventCopied);
            }
            touchEventBufferCopied.clear();
        }
    }

    public float[] getAccelerometer() {
        return accelerometer;
    }

    // implementing the method from the SensorEventListener interface but we don't use it for anything
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event) {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }

    public Typeface loadFont(String filename) {
        Typeface font = Typeface.createFromAsset(getAssets(), filename);
        if (font == null) {
            throw new RuntimeException("Could not load font from file: " + filename);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, float size) {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y+size, paint);
    }

    public void run() {

        int frames = 0;
        long lastTime = System.nanoTime();
        long currentTime = lastTime;
        while (true) {
            fixTheScreen();
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
                        //return;
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
                    Canvas canvas = surfaceHolder.lockCanvas();
                    // we will do all the drawing here

                    fillEvents();
                    currentTime = System.nanoTime();
                    if (screen != null) {
                        screen.update((currentTime - lastTime)/1000000000.0f);
                    }
                    lastTime = currentTime;
                    freeEvents();
                    src.left = 0;
                    src.top = 0;
                    // subtract 1 because pixels start at 0 but size starts at 1
                    src.right = offscreenSurface.getWidth() - 1;
                    src.bottom = offscreenSurface.getHeight() - 1;
                    dst.left = 0;
                    dst.top = 0;
                    dst.right = surfaceView.getWidth() - 1;
                    dst.bottom = surfaceView.getHeight() - 1;
                    canvas.drawBitmap(offscreenSurface, src, dst, null);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                    // releases the canvas for garbage collection cause lockCanvas creates a new .. returning memory
                    canvas = null;
                }
            }
        }
    }

    public int getFrameRate() {
        return framesPerSecond;
    }

    public void onPause() {
        super.onPause();
        synchronized (stateChanges) {
            if (isFinishing()) {
                soundPool.release();
                stateChanges.add(State.Disposed);
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            } else {
                stateChanges.add(State.Paused);
            }
        }
        mainLoopThread.interrupt();
        try {
            mainLoopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges) {
            stateChanges.add(State.Resumed);
            SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
                Sensor accSensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
        fixTheScreen();
    }

}
