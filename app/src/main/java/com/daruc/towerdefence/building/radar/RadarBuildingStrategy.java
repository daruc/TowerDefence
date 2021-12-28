package com.daruc.towerdefence.building.radar;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class RadarBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Radar.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building== null) {
            Radar radar = new Radar(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
            gameMap.setBuilding(mapPoint, radar);
            mapView.setGold(mapView.getGold() - Radar.COST);
        }
    }
}
