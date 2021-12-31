package com.daruc.towerdefence.building.squaretower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class SquareTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return mapView.getGold() >= SquareTower.COST;
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildSquareTower(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GroundType groundType = mapView.getGameMap().getGround(mapPoint);
        Building building = mapView.getGameMap().getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null;
    }

    private void buildSquareTower(MapView mapView, MapPoint mapPoint) {
        SquareTower squareTower = newSquareTower(mapView, mapPoint);
        GameMap gameMap = mapView.getGameMap();
        setNewBuildingOnMap(gameMap, squareTower, mapPoint);
        decreaseGold(mapView);
    }

    private SquareTower newSquareTower(MapView mapView, MapPoint mapPoint) {
        GroundType[][] groundTiles = mapView.getGameMap().getGroundTiles();

        return new SquareTower(new PointF(mapPoint.getX() + 0.5f,mapPoint.getY() + 0.5f),
                groundTiles);
    }

    private void setNewBuildingOnMap(GameMap gameMap, SquareTower squareTower, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, squareTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - SquareTower.COST);
    }
}
