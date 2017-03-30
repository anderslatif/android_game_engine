package dk.kea.class2017.anders.gameengine.Breakout;


public interface CollisionListener {

    void collisionWall();
    void collisionPaddle();
    void collisionBlock();

}
