package com.daruc.towerdefence_02.buildings;

import android.graphics.PointF;

/**
 * Created by darek on 04.04.18.
 */

public abstract class Building {
    protected PointF position;

    public Building(PointF position) {
        this.position = position;
    }

    public abstract int getCost();

    public PointF getPosition() {
        return position;
    }
}
