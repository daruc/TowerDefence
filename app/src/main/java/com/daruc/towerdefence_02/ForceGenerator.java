package com.daruc.towerdefence_02;

import android.graphics.PointF;

/**
 * Created by darek on 06.04.18.
 */

public class ForceGenerator extends Building{
    public ForceGenerator(PointF position) {
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
