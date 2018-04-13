package com.daruc.towerdefence;

import android.graphics.PointF;

/**
 * Created by darek on 05.04.18.
 */

public class Vectors {
    public static float distance(PointF v1, PointF v2) {
        float x = v2.x - v1.x;
        float y = v2.y - v1.y;

        return (float) Math.sqrt(x * x + y * y);
    }

    public static PointF unitVector(PointF v1, PointF v2) {
        float x = v2.x - v1.x;
        float y = v2.y - v1.y;
        float distance = (float) Math.sqrt(x * x + y * y);
        x /= distance;
        y /= distance;
        return new PointF(x, y);
    }

    public static PointF copy(PointF p) {
        return new PointF(p.x, p.y);
    }
}
