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
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.PATH && building == null) {
            Wall wall = new Wall(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
            gameMap.setBuilding(mapPoint, wall);
            mapView.setGold(mapView.getGold() - Wall.COST);
        }
    }
}
