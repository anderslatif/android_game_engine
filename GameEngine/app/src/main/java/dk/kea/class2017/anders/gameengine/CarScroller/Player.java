package dk.kea.class2017.anders.gameengine.CarScroller;


public class Player {

    public static final int WIDTH = 49;
    public static final int HEIGHT = 90;
    public int x; // where on the screen to draw the image
    public int y;
    public int spritex = 0;
    public int spritey = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
