package dk.kea.class2017.anders.gameengine.Breakout;


import dk.kea.class2017.anders.gameengine.GameEngine;
import dk.kea.class2017.anders.gameengine.Screen;

public class MainMenu extends GameEngine{


    @Override
    public Screen createStartScreen() {
        return new MainMenuScreen(this);
    }

}
