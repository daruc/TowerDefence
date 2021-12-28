package com.daruc.towerdefence;

import android.graphics.PointF;


public class Vector {
    private float x;
    private float y;

    public Vector(PointF pointF) {
        x = pointF.x;
        y = pointF.y;
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector getUnitVector() {
        float len = length();
        float newX = x / len;
        float newY = y / len;
        return new Vector(newX, newY);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public Vector minus(Vector otherVector) {
        float newX = x - otherVector.x;
        float newY = y - otherVector.y;
        return new Vector(newX, newY);
    }

    public Vector multiply(float scalar) {
        float newX = x * scalar;
        float newY = y * scalar;
        return new Vector(newX, newY);
    }

    public PointF convertToPointF() {
        return new PointF(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

