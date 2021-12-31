package com.daruc.towerdefence.building.areadamagetower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class AreaDamageTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return AreaDamageTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isTileFree(mapView, mapPoint)) {
            buildAreaDamageTower(mapView, mapPoint);
        }
    }

    private boolean isTileFree(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);
        return groundType == GroundType.GRASS && building == null;
    }

    private void buildAreaDamageTower(MapView mapView, MapPoint mapPoint) {
        AreaDamageTower areaDamageTower = newAreaDamageTower(mapPoint);
        placeNewBuildingOnMap(mapView, mapPoint, areaDamageTower);
        decreaseGold(mapView);
    }

    private AreaDamageTower newAreaDamageTower(MapPoint mapPoint) {
        return new AreaDamageTower(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(MapView mapView, MapPoint mapPoint,
                                       AreaDamageTower areaDamageTower) {

        GameMap gameMap = mapView.getGameMap();
        gameMap.setBuilding(mapPoint, areaDamageTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - AreaDamageTower.COST);
    }
}
