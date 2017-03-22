package dk.kea.class2017.anders.gameengine.Breakout;


public class Block {
    public static final float WIDTH = 40;
    public static final float HEIGHT = 18;
    float x;
    float y;
    int type;

    public Block(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }


}
