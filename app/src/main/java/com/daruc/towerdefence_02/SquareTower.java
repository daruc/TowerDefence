package com.daruc.towerdefence_02;

import android.graphics.PointF;

/**
 * Created by darek on 06.04.18.
 */

public class SquareTower extends Building {
    public SquareTower(PointF position) {
        super(position);
    }

    @Override
    public PointF getPosition() {
        return super.getPosition();
    }

    public static int getCost() {
        return 200;
    }
}
