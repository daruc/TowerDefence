package com.daruc.towerdefence_02;

import android.graphics.PointF;

/**
 * Created by darek on 04.04.18.
 */

public class Building {
    protected PointF position;
    public static int getCost() {
        return 0;
    }

    public Building(PointF position) {
        this.position = position;
    }

    public PointF getPosition() {
        return position;
    }
}
