package com.daruc.towerdefence.building;

import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;

public interface BuildingStrategy {
    boolean hasEnoughGold(MapView mapView);
    void build(MapView mapView, MapPoint mapPoint);
}
