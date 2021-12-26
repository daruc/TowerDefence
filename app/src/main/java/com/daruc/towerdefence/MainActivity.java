package com.daruc.towerdefence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initGameView();
        setContentView(gameView);
        Log.d("GAME", "Game activity onCreate()");
    }

    private void initGameView() {
        gameView = new GameView(this, getMapId());
    }

    private int getMapId() {
        Intent intent = getIntent();
        return intent.getIntExtra(InfinityModeMenuActivity.MAP_RESOURCE_ID, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pauseGame();
        Log.d("GAME", "Game activity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("GAME", "Game activity. onStop()");
        gameView.releaseSound();
    }
}
