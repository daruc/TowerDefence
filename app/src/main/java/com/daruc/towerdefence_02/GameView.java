package com.daruc.towerdefence_02;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by darek on 02.04.18.
 */

public class GameView extends ViewGroup {
    private Button restartButton;
    private MapView mapView;

    public GameView(final Context context) {
        super(context);
        setWillNotDraw(false);
        restartButton = new Button(context);
        mapView = new MapView(context);
        restartButton.setText("Restart");
        restartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.restartGame(context);
            }
        });
        addView(restartButton);
        addView(mapView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(l, t, r, b);
            restartButton.layout(0, b - 150, 400, b);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).draw(canvas);
        }
    }

    public void releaseSound() {
        mapView.getSoundPool().release();
    }
}
