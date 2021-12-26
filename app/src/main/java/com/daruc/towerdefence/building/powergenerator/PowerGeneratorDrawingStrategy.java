package com.daruc.towerdefence.building.powergenerator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Upgradable;
import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;


public class PowerGeneratorDrawingStrategy extends BuildingDrawingStrategy {
    private Paint paintPowerGeneratorRange;

    public PowerGeneratorDrawingStrategy(Building building) {
        super(building);

        paintPowerGeneratorRange = new Paint();
        paintPowerGeneratorRange.setStyle(Paint.Style.STROKE);
        paintPowerGeneratorRange.setColor(Color.CYAN);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {

        PointF position = new PointF(building.getPosition().x, building.getPosition().y);
        position.x -= 0.5f;
        position.y -= 0.5f;
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        int level = ((Upgradable) building).getLevel();
        Bitmap powerGeneratorBitmap = mapView.getPowerGeneratorBitmap();
        int bitmapHeight = powerGeneratorBitmap.getHeight();

        Rect source = new Rect(bitmapHeight * (level - 1),
                0,
                bitmapHeight * level,
                bitmapHeight);
        RectF destination = new RectF(position.x, position.y, position.x + tileSize, position.y + tileSize);

        canvas.drawBitmap(powerGeneratorBitmap, source, destination, null);

        if (building == mapView.getSelectedBuilding()) {
            PointF mapPosition = building.getPosition();
            RectF rect = new RectF();
            rect.left = (mapPosition.x - 1.5f) * tileSize;
            rect.right = (mapPosition.x + 1.5f) * tileSize;
            rect.top = (mapPosition.y - 1.5f) * tileSize;
            rect.bottom = (mapPosition.y + 1.5f) * tileSize;
            canvas.drawRect(rect, paintPowerGeneratorRange);
        }
    }
}
