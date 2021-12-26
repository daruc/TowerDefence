package com.daruc.towerdefence.building.wall;

import android.graphics.PointF;

import com.daruc.towerdefence.building.Building;


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
