package dk.kea.class2017.anders.gameengine;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener{

    private boolean[] isTouched = new boolean[20];
    private int[] touchX = new int[20];
    private int[] touchY = new int[20];
    private List<TouchEvent> touchEventBuffer;
    private TouchEventPool touchEventPool;

    public MultiTouchHandler(View v, List<TouchEvent> touchEventsBuffer, TouchEventPool touchEventPool) {
        v.setOnTouchListener(this);
        this.touchEventBuffer = touchEventsBuffer;
        this.touchEventPool = touchEventPool;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TouchEvent touchEvent = null;
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Action_Pointer contains two pieces of information .. one which is pointer number and the other is action number
        // we bitwise shift it in order to only get the pointer index out
        int pointerIndex = ( event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK ) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        // we just need to get the pointerId but we have to pass pointerIndex to get it
        int pointerId = event.getPointerId(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //break;   We don't need break cause action down and action pointer down should resolve into the same response
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEvent.TouchEventType.Down;
                touchEvent.pointer = pointerId;
                touchX[pointerId] = (int) event.getX(pointerIndex);
                touchEvent.x = touchX[pointerId];
                touchY[pointerId] = (int) event.getY(pointerIndex);
                touchEvent.y = touchY[pointerId];
                isTouched[pointerId] = true;
                synchronized (touchEventBuffer) {
                    touchEventBuffer.add(touchEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                //break;
            case MotionEvent.ACTION_POINTER_UP:
                //break;
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEvent.TouchEventType.Up;
                touchEvent.pointer = pointerId;
                touchX[pointerId] = (int) event.getX(pointerIndex);
                touchEvent.x = touchX[pointerId];
                touchY[pointerId] = (int) event.getY(pointerIndex);
                touchEvent.y = touchY[pointerId];
                isTouched[pointerId] = false;
                synchronized (touchEventBuffer) {
                    touchEventBuffer.add(touchEvent);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                synchronized (touchEventBuffer) {
                    for (int i = 0; i < pointerCount; i++) {
                        touchEvent = touchEventPool.obtain();
                        touchEvent.type = TouchEvent.TouchEventType.Dragged;
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerId;
                        touchX[pointerId] = (int) event.getX(pointerIndex);
                        touchEvent.x = touchX[pointerId];
                        touchY[pointerId] = (int) event.getY(pointerIndex);
                        touchEvent.y = touchY[pointerId];
                        isTouched[pointerId] = true;
                        touchEventBuffer.add(touchEvent);
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return isTouched[pointer];
    }

    @Override
    public int getTouchX(int pointer) {
        return touchX[pointer];
    }

    @Override
    public int getTouchY(int pointer) {
        return touchY[pointer];
    }
}
