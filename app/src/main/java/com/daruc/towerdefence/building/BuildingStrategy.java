package com.daruc.towerdefence.building;

import com.daruc.towerdefence.MapView;

public interface BuildingStrategy {
    boolean hasEnoughGold(MapView mapView);
    void build(MapView mapView, int mapX, int mapY);
}
