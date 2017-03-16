package dk.kea.class2017.anders.gameengine.Breakout;


public class World {

    public static final float MIN_X = 0;
    public static final float MAX_X = 319;  // minus 1 pixel because we start at pixel 0
    public static final float MIN_Y = 40; // 40 because we a top screen in our background where the ball shouldn't go
    public static final float MAX_Y = 479;
    Ball ball = new Ball();

    public void update(float deltaTime) {
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
            ball.vy = -ball.vy;
            ball.y = MAX_Y - Ball.WIDTH;
        }
    }

}
