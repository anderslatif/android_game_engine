package dk.kea.class2017.anders.gameengine.Breakout;


import android.graphics.Bitmap;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;

public class WorldRenderer {

    GameEngine game;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blockImage;
    Block block = null;

    public WorldRenderer(GameEngine game, World world) {
        this.game = game;
        this.world = world;
        ballImage = game.loadBitmap("ball.png");
        paddleImage = game.loadBitmap("paddle.png");
        blockImage = game.loadBitmap("blocks.png");
    }

    public void render() {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
        game.drawBitmap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);

        for (Block block : world.blocks) {
            game.drawBitmap(blockImage, (int)block.x, (int)block.y,
                    0, (int)(block.type*Block.HEIGHT), (int)Block.WIDTH, (int)Block.HEIGHT);
        }
    }

}
