package dk.kea.class2017.anders.gameengine.GameEngine;


public interface TouchHandler {

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);
}
