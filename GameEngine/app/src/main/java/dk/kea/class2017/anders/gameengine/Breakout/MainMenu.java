package dk.kea.class2017.anders.gameengine.Breakout;


import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class MainMenu extends GameEngine{


    @Override
    public Screen createStartScreen() {
        music = this.loadMusic("music.ogg");
        music.setLooping(true);
        return new MainMenuScreen(this);
    }

    public void onPause() {
        super.onPause();
        music.pause();
    }

    public void onResume() {
        super.onResume();
        music.play();
    }

}
