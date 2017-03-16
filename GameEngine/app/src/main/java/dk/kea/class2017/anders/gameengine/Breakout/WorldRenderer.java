package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;

public class WorldRenderer {

    GameEngine game;
    World world;
    Bitmap ballImage;

    public WorldRenderer(GameEngine game, World world) {
        this.game = game;
        this.world = world;
        ballImage = game.loadBitmap("ball.png");
    }

    public void render() {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
    }

}
