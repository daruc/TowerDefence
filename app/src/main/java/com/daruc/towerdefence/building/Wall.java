package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.building.drawingstrategy.WallDrawingStrategy;

/**
 * Created by darek on 08.04.18.
 */

public class Wall extends Building {
    public static final int COST = 500;

    @Override
    public int getCost() {
        return COST;
    }

    public Wall(PointF position) {
        super(position);
        drawingStrategy = new WallDrawingStrategy(this);
    }
}
