package dk.kea.class2017.anders.gameengine;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class SimpleScreen extends Screen {

    int x = 0;
    int y = 0;
    Bitmap bitmap;

    public SimpleScreen(GameEngine game) {
        super(game);
        Log.d("SimpleGame class", "##########################################");

        //bitmap = game.loadBitmap("bob.png");
    }

    @Override
    public void update(float deltaTime) {

        game.clearFrameBuffer(Color.BLUE);
        return;


/*        Log.d("SimpleGame class", "*****************************************");

        if (game.isTouchDown(0)) {
            x = game.getTouchX(0);
            y = game.getTouchY(0);
        }
        game.clearFrameBuffer(Color.BLUE);
        game.drawBitmap(bitmap, x, y);*/
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
