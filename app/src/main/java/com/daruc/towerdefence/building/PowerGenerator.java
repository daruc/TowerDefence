package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.building.drawingstrategy.PowerGeneratorDrawingStrategy;

/**
 * Created by darek on 06.04.18.
 */

public class PowerGenerator extends Building implements Upgradable {
    private static final int MAX_LEVEL = 10;
    public static final int COST = 30;
    private int level = 1;

    public PowerGenerator(PointF position) {
        super(position);
        drawingStrategy = new PowerGeneratorDrawingStrategy(this);
    }

    @Override
    public PointF getPosition() {
        return super.getPosition();
    }

    @Override
    public int getCost() {
        return COST;
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
}
