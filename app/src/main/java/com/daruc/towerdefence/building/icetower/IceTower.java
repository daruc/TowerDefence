package com.daruc.towerdefence.building.icetower;

import android.graphics.PointF;

import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Upgradable;


public class IceTower extends Building implements Upgradable {
    private static final int MAX_LEVEL = 1000;
    public static final int COST = 300;
    private int level = 1;

    public IceTower(PointF position) {
        super(position);
        drawingStrategy = new IceTowerDrawingStrategy(this);
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
