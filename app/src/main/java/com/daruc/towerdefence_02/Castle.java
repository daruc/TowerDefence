package com.daruc.towerdefence_02;

import android.graphics.PointF;

/**
 * Created by darek on 05.04.18.
 */

public class Castle extends Building {
    private float radius = 0.45f;
    private int health = 3;

    public Castle(PointF position) {
        super(position);
        this.position = new PointF(position.x, position.y);
    }

    public float getRadius() {
        return radius;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int hp) {
        health -= hp;
    }
}
