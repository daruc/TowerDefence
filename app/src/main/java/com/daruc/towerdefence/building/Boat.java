package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.building.drawingstrategy.BoatDrawingStrategy;

/**
 * Created by darek on 08.04.18.
 */

public class Boat extends Building implements Upgradable {

    private static final int MAX_LEVEL = 10;
    public static final int COST = 250;
    private float scope = 1.5f;
    private int level = 1;

    public Boat(PointF position) {
        super(position);
        drawingStrategy = new BoatDrawingStrategy(this);
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

    public float getScope() {
        return scope;
    }
}
