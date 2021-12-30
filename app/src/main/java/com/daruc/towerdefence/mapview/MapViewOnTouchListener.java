package com.daruc.towerdefence.mapview;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class MapViewOnTouchListener implements View.OnTouchListener {

    private MapView mapView;

    public MapViewOnTouchListener(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            setTouchCoordinates(event);
        }
        return false;
    }

    private void setTouchCoordinates(MotionEvent event) {
        mapView.setTouchCoordinates(new PointF(event.getX(), event.getY()));
    }
}
