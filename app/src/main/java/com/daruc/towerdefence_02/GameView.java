package com.daruc.towerdefence_02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daruc.towerdefence_02.buildings.RoundTower;

/**
 * Created by darek on 02.04.18.
 */

public class GameView extends ViewGroup {
    private MapView mapView;
    private Button restartButton;
    private Button nextWaveButton;
    private Button upgradeButton;
    private Button buildingsButton;
    private TextView goldView;


    public GameView(final Context context, int mapId) {
        super(context);
        setWillNotDraw(false);
        restartButton = new Button(context);
        nextWaveButton = new Button(context);
        upgradeButton = new Button(context);
        buildingsButton = new Button(context);
        goldView = new TextView(context);
        mapView = new MapView(context, mapId);
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
                RoundTower roundTower = (RoundTower) mapView.getSelectedBuilding();
                if (roundTower != null) {
                    int gold = mapView.getGold();
                    if (gold >= 50) {
                        if (roundTower.upgrade()) {
                            mapView.setGold(gold - 50);
                        }
                    }
                }
            }
        });

        buildingsButton.setText("Round RoundTower");
        buildingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Buildings");

                final String strBuildings[] = {
                        "Round RoundTower",
                        "Square RoundTower",
                        "Force Generator"
                };
                builder.setSingleChoiceItems(strBuildings, mapView.getBuildingSelectionIdx(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Building selection", strBuildings[which]);
                        mapView.setBuildingSelectionIndex(which);
                        buildingsButton.setText(strBuildings[which]);
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        int gold = mapView.getGold();
        goldView.setText("Gold: " + gold);

        addView(restartButton);
        addView(nextWaveButton);
        addView(upgradeButton);
        addView(buildingsButton);
        addView(goldView);
        addView(mapView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(l, t, r, b);

        }
        restartButton.layout(0, b - 170, 200, b);
        nextWaveButton.layout(210, b - 170, 400, b);
        upgradeButton.layout(410, b - 170, 600, b);
        buildingsButton.layout(610, b - 170, 790, b);
        goldView.layout(r - 240, b - 170, r, b);
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
