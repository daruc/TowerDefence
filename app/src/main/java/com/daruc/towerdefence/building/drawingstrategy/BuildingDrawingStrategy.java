package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Canvas;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;

/**
 * Created by darek on 08.04.18.
 */

public abstract class BuildingDrawingStrategy {
    protected Building building;

    public BuildingDrawingStrategy(Building building) {
        this.building = building;
    }

    public abstract void draw(Canvas canvas, MapView mapView);
}
