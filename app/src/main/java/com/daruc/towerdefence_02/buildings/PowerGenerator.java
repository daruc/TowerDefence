package com.daruc.towerdefence_02.buildings;

import android.graphics.PointF;

/**
 * Created by darek on 06.04.18.
 */

public class PowerGenerator extends Building{
    public PowerGenerator(PointF position) {
        super(position);
    }

    @Override
    public PointF getPosition() {
        return super.getPosition();
    }

    public static int getCost() {
        return 30;
    }
}
