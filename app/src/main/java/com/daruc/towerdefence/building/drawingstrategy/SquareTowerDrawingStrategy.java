package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Rocket;
import com.daruc.towerdefence.building.SquareTower;

/**
 * Created by darek on 08.04.18.
 */

public class SquareTowerDrawingStrategy extends BuildingDrawingStrategy {
    private Paint paintSquareTower;
    private Paint paintSquareTowerRange;
    private Paint paintRocket;

    public SquareTowerDrawingStrategy(Building building) {
        super(building);

        paintSquareTower = new Paint();
        paintSquareTower.setStyle(Paint.Style.FILL);
        paintSquareTower.setColor(Color.RED);

        paintSquareTowerRange = new Paint();
        paintSquareTowerRange.setStyle(Paint.Style.STROKE);
        paintSquareTowerRange.setStrokeWidth(5f);
        paintSquareTowerRange.setColor(Color.argb(170, 200, 160, 160));

        paintRocket = new Paint();
        paintRocket.setStyle(Paint.Style.FILL);
        paintRocket.setColor(Color.RED);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        PointF position = Vectors.copy(building.getPosition());
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;

        Bitmap squareTowerBitmap = mapView.getSquareTowerBitmap();
        int bitmapHeight = squareTowerBitmap.getHeight();
        int level = ((SquareTower) building).getLevel();
        Rect source = new Rect(bitmapHeight * (level - 1),
                0,
                bitmapHeight * level,
                bitmapHeight);

        RectF destination = new RectF();
        destination.left = position.x - (tileSize / 2);
        destination.right = position.x + (tileSize / 2);
        destination.top = position.y - (tileSize / 2);
        destination.bottom = position.y + (tileSize / 2);
        //canvas.drawRect(rect, paintSquareTower);
        canvas.drawBitmap(squareTowerBitmap, source, destination, null);


        if (building == mapView.getSelectedBuilding()) {
            canvas.drawLine(0f, position.y, mapView.getWidth(), position.y, paintSquareTowerRange);
            canvas.drawLine(position.x, 0f, position.x, mapView.getHeight(), paintSquareTowerRange);
        }

        for (Rocket rocket : ((SquareTower) building).getRockets()) {
            if (rocket.isFree()) continue;

            PointF rocketPosition = Vectors.copy(rocket.getPosition());
            rocketPosition.x *= tileSize;
            rocketPosition.y *= tileSize;

            canvas.drawCircle(rocketPosition.x, rocketPosition.y, 15f, paintRocket);
        }
    }
}
