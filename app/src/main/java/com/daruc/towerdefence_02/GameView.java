package com.daruc.towerdefence_02;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by darek on 02.04.18.
 */

public class GameView extends ViewGroup {
    private Button restartButton;
    private MapView mapView;
    private TextView goldView;
    private Button nextWaveButton;
    private Button upgradeButton;


    public GameView(final Context context) {
        super(context);
        setWillNotDraw(false);
        restartButton = new Button(context);
        nextWaveButton = new Button(context);
        upgradeButton = new Button(context);
        goldView = new TextView(context);
        mapView = new MapView(context);
        restartButton.setText("Restart");
        restartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.restartGame(context);
            }
        });

        nextWaveButton.setText("Next wave");
        nextWaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.nextWave();
            }
        });

        upgradeButton.setText("Upgrade");
        upgradeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tower tower = (Tower) mapView.getSelectedBuilding();
                if (tower != null) {
                    int gold = mapView.getGold();
                    if (gold >= 50) {
                        tower.upgrade();
                        mapView.setGold(gold - 50);
                    }
                }
            }
        });


        int gold = mapView.getGold();
        goldView.setText("Gold: " + gold);

        addView(restartButton);
        addView(nextWaveButton);
        addView(upgradeButton);
        addView(goldView);
        addView(mapView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(l, t, r, b);

        }
        restartButton.layout(0, b - 150, 250, b);
        nextWaveButton.layout(270, b - 150, 500, b);
        upgradeButton.layout(520, b - 150, 800, b);
        goldView.layout(r - 240, b - 150, r, b);
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

    public TextView getGoldView() {
        return goldView;
    }
}
