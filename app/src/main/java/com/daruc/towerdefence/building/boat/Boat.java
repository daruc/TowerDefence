package com.daruc.towerdefence.building.boat;

import android.graphics.PointF;

import com.daruc.towerdefence.Vector;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Upgradable;

import java.util.List;


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
            Vector direction = calculateDirection();
            Vector offset = direction.multiply((deltaTimeMillis / 1000.0f) * moveSpeed);
            PointF offsetPointF = offset.convertToPointF();
            position.x += offsetPointF.x;
            position.y += offsetPointF.y;

            if (calculateDistance() < 0.05f) {
                position = movePath.get(0);
                movePath.remove(0);
            }
        }
    }

    public Vector calculateDirection() {
        Vector positionVector = new Vector(position);
        Vector movePathVector = new Vector(movePath.get(0));
        return movePathVector.minus(positionVector).getUnitVector();
    }

    public float calculateDistance() {
        Vector positionVector = new Vector(position);
        Vector movePathVector = new Vector(movePath.get(0));
        return positionVector.minus(movePathVector).length();
    }
}
