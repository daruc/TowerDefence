package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.drawingstrategy.BoatDrawingStrategy;

import java.util.List;

/**
 * Created by darek on 08.04.18.
 */

public class Boat extends Building implements Upgradable {

    private static final int MAX_LEVEL = 10;
    public static final int COST = 250;
    private float scope = 1.5f;
    private int level = 1;

    private List<PointF> movePath = null;
    private float moveSpeed = 1f;

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

    public void setPath(List<PointF> path) {
        movePath = path;
    }

    public void move(long deltaTimeMillis) {
        if (movePath != null && !movePath.isEmpty()) {
            PointF direction = Vectors.unitVector(position, movePath.get(0));
            position.x += direction.x * (deltaTimeMillis / 1000.0f) * moveSpeed;
            position.y += direction.y * (deltaTimeMillis / 1000.0f) * moveSpeed;

            if (Vectors.distance(position, movePath.get(0)) < 0.05f) {
                position = movePath.get(0);
                movePath.remove(0);
            }
        }
    }
}
