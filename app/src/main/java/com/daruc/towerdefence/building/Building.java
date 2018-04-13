package com.daruc.towerdefence.building;

import android.graphics.PointF;
import android.os.Build;

import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;

/**
 * Created by darek on 04.04.18.
 */

public abstract class Building {
    protected PointF position;
    protected BuildingDrawingStrategy drawingStrategy;

    public Building(PointF position) {
        this.position = position;
    }

    public abstract int getCost();

    public BuildingDrawingStrategy getDrawingStrategy() {
        return drawingStrategy;
    }

    public PointF getPosition() {
        return position;
    }
}
