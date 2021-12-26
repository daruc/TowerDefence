package com.daruc.towerdefence.building.areadamagetower;

import android.graphics.PointF;

import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Upgradable;


public class AreaDamageTower extends Building implements Upgradable {
    private static final int MAX_LEVEL = 1000;
    public static final int COST = 300;
    private int level = 1;

    public AreaDamageTower(PointF position) {
        super(position);
        drawingStrategy = new AreaDamageTowerDrawingStrategy(this);
    }

    @Override
    public boolean upgrade() {
        if (getLevel() < getMaxLevel()) {
            ++level;
            return true;
        }
        return false;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public int getCost() {
        return COST;
    }
}
