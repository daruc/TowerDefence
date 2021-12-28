package com.daruc.towerdefence;


import android.graphics.PointF;

public class MapPoint {
    private int x;
    private int y;

    public MapPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapPoint(PointF pointF) {
        x = (int) pointF.x;
        y = (int) pointF.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPoint mapPoint = (MapPoint) o;

        if (x != mapPoint.x) return false;
        return y == mapPoint.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
