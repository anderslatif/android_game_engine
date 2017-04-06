package dk.kea.class2017.anders.gameengine.CarScroller;


import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;
import dk.kea.class2017.anders.gameengine.GameEngine.Screen;

public class CarScroller extends GameEngine {

    @Override
    public Screen createStartScreen() {
        music = this.loadMusic("breakout/music.ogg");
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

    public void onDestroy() {
        super.onDestroy();
        music.stop();
        music.dispose();
    }

}
