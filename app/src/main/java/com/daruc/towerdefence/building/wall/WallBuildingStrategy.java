package com.daruc.towerdefence.building.wall;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class WallBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Wall.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildWall(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.PATH && building == null;
    }

    private void buildWall(MapView mapView, MapPoint mapPoint) {
        Wall wall = newWall(mapPoint);
        GameMap gameMap = mapView.getGameMap();
        placeNewWallOnMap(gameMap, wall, mapPoint);
        decreaseGold(mapView);
    }

    private Wall newWall(MapPoint mapPoint) {
        return new Wall(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewWallOnMap(GameMap gameMap, Wall wall, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, wall);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - Wall.COST);
    }
}
