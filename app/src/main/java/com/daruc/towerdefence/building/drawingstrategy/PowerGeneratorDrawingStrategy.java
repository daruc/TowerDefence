package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.Building;

/**
 * Created by darek on 08.04.18.
 */

public class PowerGeneratorDrawingStrategy extends BuildingDrawingStrategy {
    private Paint paintPowerGenerator;
    private Paint paintPowerGeneratorRange;

    public PowerGeneratorDrawingStrategy(Building building) {
        super(building);

        paintPowerGenerator = new Paint();
        paintPowerGenerator.setStyle(Paint.Style.FILL);
        paintPowerGenerator.setColor(Color.CYAN);

        paintPowerGeneratorRange = new Paint();
        paintPowerGeneratorRange.setStyle(Paint.Style.STROKE);
        paintPowerGeneratorRange.setColor(Color.CYAN);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        PointF position = Vectors.copy(building.getPosition());
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;
        canvas.drawCircle(position.x, position.y, tileSize / 2 - 10, paintPowerGenerator);

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
