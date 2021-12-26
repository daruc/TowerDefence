package com.daruc.towerdefence.building.boat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;


public class BoatDrawingStrategy extends BuildingDrawingStrategy {

    private Paint paintBoatRange;

    public BoatDrawingStrategy(Building building) {
        super(building);

        paintBoatRange = new Paint();
        paintBoatRange.setStyle(Paint.Style.STROKE);
        paintBoatRange.setStrokeWidth(5f);
        paintBoatRange.setColor(Color.argb(170, 169, 78, 78));
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {

        PointF position = new PointF(building.getPosition().x, building.getPosition().y);
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        Bitmap boatBitmap = mapView.getBoatBitmap();
        int bitmapHeight = boatBitmap.getHeight();
        int level = ((Boat) building).getLevel();

        Rect source = new Rect(bitmapHeight * (level - 1),
                0,
                bitmapHeight * level,
                bitmapHeight);

        RectF destination = new RectF(position.x - (tileSize / 2),
                position.y - (tileSize / 2),
                position.x + (tileSize / 2),
                position.y + (tileSize / 2));

        canvas.drawBitmap(boatBitmap, source, destination, null);

        Boat boat = (Boat) building;
        float scope = boat.getScope() * tileSize;
        if (building == mapView.getSelectedBuilding()) {
            canvas.drawCircle(position.x, position.y, scope, paintBoatRange);
        }
    }
}
