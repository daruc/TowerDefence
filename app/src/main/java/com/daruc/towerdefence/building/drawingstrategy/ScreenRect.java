package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.PointF;
import android.graphics.RectF;

import com.daruc.towerdefence.MapView;

public class ScreenRect {

    private RectF rect;

    public ScreenRect(MapView mapView, PointF screenPosition) {
        float halfTile = mapView.getTileSize() / 2.0f;
        rect = new RectF(screenPosition.x - halfTile,
                screenPosition.y - halfTile,
                screenPosition.x + halfTile,
                screenPosition.y + halfTile);
    }

    public RectF getRect() {
        return rect;
    }
}
