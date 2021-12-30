package com.daruc.towerdefence.building.radar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;

/**
 * Created by darek on 08.04.18.
 */

public class RadarDrawingStrategy extends BuildingDrawingStrategy {
    public RadarDrawingStrategy(Building building) {
        super(building);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        // requires implementation
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 100, 122, 133));
        paint.setStyle(Paint.Style.FILL);

        PointF position = new PointF(building.getPosition().x, building.getPosition().y);
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        canvas.drawCircle(position.x, position.y, tileSize / 3f, paint);
    }
}
