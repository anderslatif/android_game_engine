package dk.kea.class2017.anders.gameengine.GameEngine;


abstract public class Screen {

    protected final GameEngine game;

    public Screen(GameEngine game) {
        this.game = game;
    }

    // adjusting the speed of the game, a thread of its own
    public abstract void update(float deltaTime);

    public abstract void pause();
    public abstract void resume();

    // when the user swipes away the android system gives the program a chance to save data/ create pause
    // or when killing one activity and starting another ... like going from level 1 to 2 (restarting an app from the systems piont of view)
    public abstract void dispose();



}
