package dk.kea.class2017.anders.gameengine.Breakout;


import java.util.ArrayList;
import java.util.List;

import dk.kea.class2017.anders.gameengine.GameEngine.GameEngine;

public class World {

    public static final float MIN_X = 0;
    public static final float MAX_X = 319;  // minus 1 pixel because we start at pixel 0
    public static final float MIN_Y = 40; // 40 because there is a top screen in our background where the ball shouldn't go
    public static final float MAX_Y = 479;
    boolean gameOver = false;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<>();
    GameEngine game;

    public World(GameEngine game) {
        generateBlocks();
        this.game = game;
    }

    public void update(float deltaTime, float accelX) {

        ball.x = ball.x + ball.vx * deltaTime;
        ball.y = ball.y + ball.vy * deltaTime;
        if (ball.x < MIN_X) {
            ball.vx = -ball.vx;
            ball.x = MIN_X;
        }
        if (ball.x > MAX_X - Ball.WIDTH) {
            ball.vx = -ball.vx;
            ball.x = MAX_X - Ball.WIDTH;
        }
        if (ball.y < MIN_Y) {
            ball.vy = -ball.vy;
            ball.y = MIN_Y;
        }
        if (ball.y > MAX_Y - Ball.HEIGHT) {
            gameOver = true;
            return;
        }

        if (game.isTouchDown(0)) {
            if (game.getTouchY(0) > game.getFrameBufferHeight()*2/3) {
                paddle.x = game.getTouchX(0) - Paddle.WIDTH/2;
            }
        }

        paddle.x = paddle.x - accelX * deltaTime * 50; // negating the accelerometer
        if (paddle.x < MIN_X) {
            paddle.x = MIN_X;
        }
        if (paddle.x + Paddle.WIDTH > MAX_X) {  // add width because we check if the right side of the paddleImage touches the right border
            paddle.x = MAX_X - Paddle.WIDTH;
        }
        collideBallWithPaddle();
        collideBallWithBlocks();
    }

    private void generateBlocks() {
        blocks.clear();
        // starting at 60 because the top of the background is 40 pixels
        for (int y = 60, type = 0; y < 60 + (Block.HEIGHT+5)*8; y+=(int)Block.HEIGHT+5, type++) {  // the rows
            for (int x = 20; x < MAX_X-Block.WIDTH; x+=(int)Block.WIDTH+5) { // columns
                blocks.add(new Block(x, y, type));
            }
        }
    }

    private void collideBallWithPaddle() {
        if (ball.x + Ball.WIDTH/3*2 > paddle.x && ball.x < paddle.x + Paddle.WIDTH && ball.y + Ball.HEIGHT > paddle.y) {
            ball.vy = - ball.vy;
            ball.y = paddle.y - Ball.HEIGHT;
        }
    }

    // giving two rectangles (ball image/square image) and see if they collide
    private boolean collideRects(float x, float y, float width, float height,
                                       float x2, float y2, float width2, float height2) {

        if (x < x2 + width2 && x+width > x2 && y < y2+height2 && y+height > y2) {
            return true;
        }
        return false;
    }

    private void collideBallWithBlocks() {
        for (int i=0; i<blocks.size();i++) {
            Block block = blocks.get(i);
            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT)) {
                blocks.remove(i);
                i=i-1;
            }
        }
        /*for (Block block : blocks) {
            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                            block.x, block.y, Block.WIDTH, Block.HEIGHT)) {
                blocks.remove(block);
            }
        }*/
    }

}
