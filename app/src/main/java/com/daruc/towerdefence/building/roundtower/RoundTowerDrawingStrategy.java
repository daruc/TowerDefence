package com.daruc.towerdefence.building.roundtower;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.daruc.towerdefence.building.BuildingBitmapRect;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.drawingstrategy.BuildingDrawingStrategy;
import com.daruc.towerdefence.building.drawingstrategy.ScreenRect;


public class RoundTowerDrawingStrategy extends BuildingDrawingStrategy {

    private static final int RANGE_LINE_COLOR =
            Color.argb(170, 160, 160, 200);

    private static final float RANGE_LINE_WIDTH = 5f;

    private Paint paintTowerRange;
    private Paint paintBullet;

    public RoundTowerDrawingStrategy(Building building) {
        super(building);

        paintTowerRange = new Paint();
        paintTowerRange.setStyle(Paint.Style.STROKE);
        paintTowerRange.setStrokeWidth(RANGE_LINE_WIDTH);
        paintTowerRange.setColor(RANGE_LINE_COLOR);

        paintBullet = new Paint();
        paintBullet.setStyle(Paint.Style.FILL);
        paintBullet.setColor(Color.DKGRAY);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        RoundTower roundTower = (RoundTower) building;
        PointF screenPosition = calculateScreenPosition(mapView);
        int tileSize = mapView.getTileSize();

        drawBuilding(canvas, mapView, screenPosition);
        drawRangeCircleIfSelected(canvas, mapView, roundTower, screenPosition, tileSize);
        drawBullets(canvas, roundTower, tileSize);
    }

    private void drawBuilding(Canvas canvas, MapView mapView, PointF screenPosition) {
        Bitmap roundTowerBitmap = mapView.getRoundTowerBitmap();
        int level = getBuildingLevel();

        Rect source = new BuildingBitmapRect(roundTowerBitmap, level).getRect();
        RectF destination = new ScreenRect(mapView, screenPosition).getRect();

        canvas.drawBitmap(roundTowerBitmap, source, destination, null);
    }

    private void drawRangeCircleIfSelected(Canvas canvas, MapView mapView, RoundTower roundTower,
                                           PointF screenPosition, int tileSize) {

        if (building == mapView.getSelectedBuilding()) {
            float scope = roundTower.getScope() * tileSize;
            canvas.drawCircle(screenPosition.x, screenPosition.y, scope, paintTowerRange);
        }
    }

    private int getBuildingLevel() {
        return ((RoundTower) building).getLevel();
    }

    private PointF calculateScreenPosition(MapView mapView) {
        PointF position = new PointF(building.getPosition().x, building.getPosition().y);
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;
        return position;
    }

    private void drawBullets(Canvas canvas, RoundTower roundTower, int tileSize) {
        for (Bullet bullet : roundTower.getBullets()) {
            if (bullet.isFree()) {
                continue;
            }

            drawBullet(canvas, tileSize, bullet);
        }
    }

    private void drawBullet(Canvas canvas, int tileSize, Bullet bullet) {
        PointF bulletPosition = new PointF(bullet.getPosition().x, bullet.getPosition().y);
        bulletPosition.x *= tileSize;
        bulletPosition.y *= tileSize;

        canvas.drawCircle(bulletPosition.x, bulletPosition.y, 10f, paintBullet);
    }
}
