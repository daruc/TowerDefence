package com.daruc.towerdefence.building.roundtower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class RoundTowerBuildingStrategy implements BuildingStrategy {

    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return RoundTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildRoundTower(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null;
    }

    private void buildRoundTower(MapView mapView, MapPoint mapPoint) {
        RoundTower roundTower = newRoundTower(mapView, mapPoint);
        GameMap gameMap = mapView.getGameMap();
        setNewBuildingOnMap(gameMap, roundTower, mapPoint);
        decreaseGold(mapView);
    }

    private RoundTower newRoundTower(MapView mapView, MapPoint mapPoint) {
        PointF coordinates = new PointF(mapPoint.getX() + 0.5f,
                mapPoint.getY() + 0.5f);
        return new RoundTower(mapView, coordinates);
    }

    private void setNewBuildingOnMap(GameMap gameMap, RoundTower roundTower, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, roundTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - RoundTower.COST);
    }
}
