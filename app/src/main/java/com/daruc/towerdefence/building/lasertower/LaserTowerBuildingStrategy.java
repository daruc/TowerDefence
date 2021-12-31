package com.daruc.towerdefence.building.lasertower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class LaserTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return LaserTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildLaserTower(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null;
    }

    private void buildLaserTower(MapView mapView, MapPoint mapPoint) {
        LaserTower laserTower = newLaserTower(mapPoint);
        GameMap gameMap = mapView.getGameMap();
        placeNewBuildingOnMap(gameMap, laserTower, mapPoint);
        decreaseGold(mapView);
    }

    private LaserTower newLaserTower(MapPoint mapPoint) {
        return new LaserTower(new PointF(mapPoint.getX() + 0.5f,
                mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(GameMap gameMap, LaserTower laserTower, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, laserTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - LaserTower.COST);
    }
}
