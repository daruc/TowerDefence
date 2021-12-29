package com.daruc.towerdefence.building.drawingstrategy;

import com.daruc.towerdefence.DrawingStrategy;
import com.daruc.towerdefence.building.Building;

public abstract class BuildingDrawingStrategy implements DrawingStrategy {
    protected Building building;

    public BuildingDrawingStrategy(Building building) {
        this.building = building;
    }
}
