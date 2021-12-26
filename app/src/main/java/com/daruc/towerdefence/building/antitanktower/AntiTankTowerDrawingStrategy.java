package com.daruc.towerdefence.building.antitanktower;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;


public class AntiTankTowerDrawingStrategy extends BuildingDrawingStrategy {

    public AntiTankTowerDrawingStrategy(Building building) {
        super(building);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        // requires implementation
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 134, 133, 40));
        paint.setStyle(Paint.Style.FILL);

        PointF position = new PointF(building.getPosition().x, building.getPosition().y);
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        canvas.drawCircle(position.x, position.y, tileSize / 3f, paint);
    }
}
