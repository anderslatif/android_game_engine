package dk.kea.class2017.anders.gameengine.GameEngine;


public class TouchEventPool extends Pool<TouchEvent> {


    @Override
    protected TouchEvent newItem() {
        return new TouchEvent();
    }
}
