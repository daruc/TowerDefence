package com.daruc.towerdefence.building.radar;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class RadarBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Radar.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildRadar(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building== null;
    }

    private void buildRadar(MapView mapView, MapPoint mapPoint) {
        Radar radar = newRadar(mapPoint);
        GameMap gameMap = mapView.getGameMap();
        placeNewRadarOnMap(gameMap, radar, mapPoint);
        decreaseGold(mapView);
    }

    private Radar newRadar(MapPoint mapPoint) {
        return new Radar(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewRadarOnMap(GameMap gameMap, Radar radar, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, radar);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - Radar.COST);
    }
}
