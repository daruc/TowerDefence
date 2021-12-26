package com.daruc.towerdefence.building.castle;

import android.graphics.PointF;

import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.castle.CastleDrawingStrategy;


public class Castle extends Building {
    private float radius = 0.45f;

    private final int maxHealth = 3;
    private int health = maxHealth;

    public Castle(PointF position) {
        super(position);
        this.position = new PointF(position.x, position.y);
        drawingStrategy = new CastleDrawingStrategy(this);
    }

    @Override
    public int getCost() {
        return 0;
    }

    public float getRadius() {
        return radius;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int hp) {
        if (health > 0) {
            health -= hp;
        }
    }
}
