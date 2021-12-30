package com.daruc.towerdefence;

import android.graphics.Canvas;

import com.daruc.towerdefence.mapview.MapView;

public interface DrawingStrategy {
    void draw(Canvas canvas, MapView mapView);
}
