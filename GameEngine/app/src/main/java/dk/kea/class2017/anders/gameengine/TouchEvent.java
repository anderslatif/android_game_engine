package dk.kea.class2017.anders.gameengine;


public class TouchEvent {

    // Android maps 5 different touch types but we map them to basically 3 different types
    public enum TouchEventType {
        Down,
        Up,
        Dragged
    }

    public TouchEventType type;
    // x,y position of the touch
    public int x;
    public int y;
    // pointer id from the android system
    public int pointer;
}
