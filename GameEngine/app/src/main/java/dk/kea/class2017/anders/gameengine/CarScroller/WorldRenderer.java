package dk.kea.class2017.anders.gameengine.CarScroller;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;

public class WorldRenderer {

    GameEngine game;
    World world;
    Bitmap scrBackImage;
    Bitmap carImage;
    Bitmap monsterImage;
    Bitmap playerImage;
    int offScreenSurfaceWidth;
    int offScreenSurfaceHeight;

    public WorldRenderer(GameEngine game, World world) {
        this.game = game;
        this.world = world;
        scrBackImage = game.loadBitmap("carscroller/carbackground.png");
        carImage = game.loadBitmap("carscroller/bluecar2.png");
        monsterImage = game.loadBitmap("carscroller/yellowmonster.png");
        playerImage = game.loadBitmap("carscroller/runningalien.png");
        offScreenSurfaceWidth = game.getFrameBufferWidth();
        offScreenSurfaceHeight = game.getFrameBufferHeight();
    }


    public void render() {
        game.drawBitmap(scrBackImage, 0, 0, (int)world.scrollingBackground.scrollx, 0,
                offScreenSurfaceWidth, offScreenSurfaceHeight);
        //game.drawBitmap(carImage, 30, 160);
        game.drawBitmap(playerImage, world.player.x, world.player.y, world.player.spritex, world.player.spritey,
                        Player.WIDTH, Player.HEIGHT);
    }

}
