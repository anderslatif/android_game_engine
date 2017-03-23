package dk.kea.class2017.anders.gameengine.Breakout;


public class Paddle {

    public static final float WIDTH = 56;
    public static final float HEIGHT = 11;

    float x = 160 - WIDTH/2; //game.getFramebufferWidth/2
    float y = World.MAX_Y - 35; //35 is how much air to the bottom the paddleImage will have

}
