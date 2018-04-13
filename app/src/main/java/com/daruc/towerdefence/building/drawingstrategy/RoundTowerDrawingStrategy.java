package com.daruc.towerdefence.building.drawingstrategy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.Vectors;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.RoundTower;

/**
 * Created by darek on 08.04.18.
 */

public class RoundTowerDrawingStrategy extends BuildingDrawingStrategy {

    private Paint paintTower;
    private Paint paintTowerRange;
    private Paint paintBullet;

    public RoundTowerDrawingStrategy(Building building) {
        super(building);

        paintTower = new Paint();
        paintTower.setColor(Color.BLUE);
        paintTower.setStyle(Paint.Style.FILL);

        paintTowerRange = new Paint();
        paintTowerRange.setStyle(Paint.Style.STROKE);
        paintTowerRange.setStrokeWidth(5f);
        paintTowerRange.setColor(Color.argb(170, 160, 160, 200));

        paintBullet = new Paint();
        paintBullet.setStyle(Paint.Style.FILL);
        paintBullet.setColor(Color.DKGRAY);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        RoundTower roundTower = (RoundTower) building;
        PointF position = Vectors.copy(building.getPosition());
        int tileSize = mapView.getTileSize();
        position.x *= tileSize;
        position.y *= tileSize;
        float radius = roundTower.getRadius() * tileSize;
        canvas.drawCircle(position.x, position.y, radius, paintTower);
        float scope = roundTower.getScope() * tileSize;
        if (building == mapView.getSelectedBuilding()) {
            canvas.drawCircle(position.x, position.y, scope, paintTowerRange);
        }

        for (Bullet bullet : roundTower.getBullets()) {
            if (bullet.isFree()) continue;

            PointF bulletPosition = Vectors.copy(bullet.getPosition());
            bulletPosition.x *= tileSize;
            bulletPosition.y *= tileSize;

            canvas.drawCircle(bulletPosition.x, bulletPosition.y, 10f, paintBullet);
        }
    }

}
