package dk.kea.class2017.anders.gameengine.GameEngine;


public class SimpleGame extends GameEngine {

    @Override
    public Screen createStartScreen() {

        return new SimpleScreen(this);
    }


}
