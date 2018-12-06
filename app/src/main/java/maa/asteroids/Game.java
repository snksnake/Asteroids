package maa.asteroids;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class Game extends Activity {

    private GameView view;
    private long stoppedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        view = findViewById(R.id.game_view);
    }

    @Override
    protected void onDestroy() {
        view.getThread().stopGame();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.getThread().continueGame();
        if(stoppedTime>0){
            stoppedTime = System.currentTimeMillis() - stoppedTime;
        }
        view.enableSensors(this);
        stoppedTime = 0;
    }

    @Override
    protected void onPause() {
        view.getThread().pauseGame();
        view.disableSensors(this);
        stoppedTime += System.currentTimeMillis();
        super.onPause();
    }

}
