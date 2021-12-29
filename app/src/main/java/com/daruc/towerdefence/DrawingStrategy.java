package com.daruc.towerdefence;

import android.graphics.Canvas;

public interface DrawingStrategy {
    void draw(Canvas canvas, MapView mapView);
}
