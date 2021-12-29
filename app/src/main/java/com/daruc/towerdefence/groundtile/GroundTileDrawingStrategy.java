package com.daruc.towerdefence.groundtile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.MapView;

public abstract class GroundTileDrawingStrategy {

    protected int bitmapId;
    protected Bitmap bitmap;

    public GroundTileDrawingStrategy(int bitmapId) {
        this.bitmapId = bitmapId;
    }

    public void draw(Canvas canvas, MapView mapView, MapPoint mapPoint) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mapView.getResources(), bitmapId);
        }

        float tileSize = mapView.getTileSize();
        Rect source = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF dest = new RectF(mapPoint.getX(), mapPoint.getY(), mapPoint.getX() + tileSize,
                mapPoint.getY() + tileSize);
        canvas.drawBitmap(bitmap, source, dest, null);
    }
}
