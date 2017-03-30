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
    GameEngine game;
    CollisionListener collisionListener;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<>();
    int points = 0;

    public World(GameEngine game, CollisionListener collisionListener) {
        this.game = game;
        this.collisionListener = collisionListener;
        generateBlocks();
    }

    public void update(float deltaTime, float accelX) {

        ball.x = ball.x + ball.vx * deltaTime;
        ball.y = ball.y + ball.vy * deltaTime;
        if (ball.x < MIN_X) {
            ball.vx = -ball.vx;
            ball.x = MIN_X;
            collisionListener.collisionWall();
        }
        if (ball.x > MAX_X - Ball.WIDTH) {
            ball.vx = -ball.vx;
            ball.x = MAX_X - Ball.WIDTH;
            collisionListener.collisionWall();
        }
        if (ball.y < MIN_Y) {
            ball.vy = -ball.vy;
            ball.y = MIN_Y;
            collisionListener.collisionWall();
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
        collideBallWithBlocks(deltaTime);

        if (blocks.size() == 0) {
            generateBlocks();
        }

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
        // adding some pixels to the paddle so that we make sure that ball actually hits the paddle and not the two corners of the image
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, paddle.x, paddle.y, 1, Paddle.HEIGHT/2)) {
            if (ball.vx > 0) {
                // if the ball is coming from the left then invert both
                ball.vx = -ball.vx;
            }
            ball.vy = -ball.vy;
            ball.y = paddle.y - Ball.HEIGHT - 1;
            collisionListener.collisionPaddle();
            return;
        }

        // right corner
        if (collideRects(ball.x, ball.y + Ball.HEIGHT, Ball.WIDTH, 1, paddle.x + Paddle.WIDTH, paddle.y, 1, Paddle.HEIGHT/2)) {
            if (ball.vx < 0) {
                // if it comes from the right side towards left, then invert both
                ball.vx = -ball.vx;
            }
            ball.vy = - ball.vy;
            ball.y = paddle.y - Ball.HEIGHT - 1;
            return;
        }

        // check middle part of paddle top edge
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, paddle.x, paddle.y, Paddle.WIDTH, 1)) {
        /*if (ball.x + Ball.WIDTH > paddle.x && ball.x < paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT > paddle.y && ball.y + Ball.HEIGHT < paddle.y + 3) {*/
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

    private void collideBallWithBlocks(float deltaTime) {
        for (int i=0; i<blocks.size();i++) {
            Block block = blocks.get(i);
            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT)) {
                points = points + 10 - block.type;
                blocks.remove(i);
                i=i-1; // since the block has been removed the next to check is now at index minus 1
                float oldvx = ball.vx;
                float oldvy = ball.vy;
                reflectBall(ball, block);
                ball.x = ball.x - oldvx * deltaTime * 1.01f;
                ball.y = ball.y - oldvy * deltaTime * 1.01f;
                collisionListener.collisionBlock();
            }
        }
    }

    private void reflectBall(Ball ball, Block block) {
        // check the top left corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, 1)) {
            if (ball.vx > 0) {
                ball.vx = -ball.vx;
            }
            if (ball.vy > 0) {
                ball.vy = -ball.vy;
            }
            return;
        }
        // check the top right corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x+Block.WIDTH, block.y, 1, 1)) {
            if (ball.vx < 0) {
                ball.vx = -ball.vx;
            }
            if (ball.vy < 0) {
                ball.vy = -ball.vy;
            }
            return;
        }
        // check the bottom left corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y+Block.HEIGHT, 1, 1)) {
            if (ball.vx > 0) {
                ball.vx = -ball.vx;
            }
            if (ball.vy < 0) {
                ball.vy = -ball.vy;
            }
            return;
        }

        // check the bottom right corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x+Block.WIDTH, block.y+Block.HEIGHT, 1, 1)) {
            if (ball.vx < 0) {
                ball.vx = -ball.vx;
            }
            if (ball.vy < 0) {
                ball.vy = -ball.vy;
            }
            return;
        }

        // check the top of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, 1)) {
            // if get here the if statement is logically not needed but it makes the code more logically sound and avoid lags causing bugs
            if (ball.vy > 0) {
                ball.vy = -ball.vy;
            }
            return;
        }

        // check the right side of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                        block.x+Block.WIDTH, block.y, 1, Block.HEIGHT)) {
            if (ball.vx > 0) {
                ball.vx = -ball.vx;
            }
            return;
        }

        // check the bottom of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                        block.x, block.y + Block.HEIGHT, Block.WIDTH, 1)) {
            if (ball.vy < 0) {
                ball.vy = -ball.vy;
            }
            return;
        }

        // check the left side of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y, 1, Block.HEIGHT)) {
            if (ball.vx > 0) {
                ball.vx = -ball.vx;
            }
        }

    }

}
