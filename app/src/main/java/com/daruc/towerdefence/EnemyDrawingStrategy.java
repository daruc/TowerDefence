package com.daruc.towerdefence;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;


public class EnemyDrawingStrategy implements DrawingStrategy {

    private Enemy enemy;
    private Paint paintEnemy;

    public EnemyDrawingStrategy(Enemy enemy) {
        this.enemy = enemy;
        initPaint();
    }

    private void initPaint() {
        paintEnemy = new Paint();
        paintEnemy.setColor(Color.BLACK);
        paintEnemy.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView) {
        int tileSize = mapView.getTileSize();
        PointF position = enemy.getPosition();
        position = new PointF(position.x, position.y);
        position.x *= tileSize;
        position.y *= tileSize;
        canvas.drawCircle(position.x, position.y, enemy.getRadius() * tileSize,
                paintEnemy);
    }
}
