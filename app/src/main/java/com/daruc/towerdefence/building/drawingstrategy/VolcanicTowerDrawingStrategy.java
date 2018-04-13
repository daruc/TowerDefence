package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.Building;

/**
 * Created by darek on 08.04.18.
 */

public class VolcanicTowerDrawingStrategy extends BuildingDrawingStrategy {
    public VolcanicTowerDrawingStrategy(Building building) {
        super(building);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        // requires implementation
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 12, 23, 55));
        paint.setStyle(Paint.Style.FILL);

        PointF position = Vectors.copy(building.getPosition());
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        canvas.drawCircle(position.x, position.y, tileSize / 3f, paint);
    }
}
