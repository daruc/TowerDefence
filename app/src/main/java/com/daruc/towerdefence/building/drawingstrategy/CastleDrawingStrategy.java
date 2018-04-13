package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Castle;

/**
 * Created by darek on 08.04.18.
 */

public class CastleDrawingStrategy extends BuildingDrawingStrategy {
    private Paint paintCastle;

    public CastleDrawingStrategy(Building building) {
        super(building);

        paintCastle = new Paint();
        paintCastle.setStyle(Paint.Style.FILL);
        paintCastle.setColor(Color.LTGRAY);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        int tileSize = mapView.getTileSize();
        float radius = ((Castle) building).getRadius() * tileSize;
        PointF position = Vectors.copy(building.getPosition());
        position.x *= tileSize;
        position.y *= tileSize;
        canvas.drawCircle(position.x, position.y, radius, paintCastle);
    }
}
