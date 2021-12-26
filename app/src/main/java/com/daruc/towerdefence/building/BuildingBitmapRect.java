package com.daruc.towerdefence.building;


import android.graphics.Bitmap;
import android.graphics.Rect;

public class BuildingBitmapRect {

    private Rect rect;

    public BuildingBitmapRect(Bitmap bitmap, int level) {
        int bitmapHeight = bitmap.getHeight();
        rect = new Rect(bitmapHeight * (level - 1),
                0,
                bitmapHeight * level,
                bitmapHeight);
    }

    public Rect getRect() {
        return rect;
    }
}
