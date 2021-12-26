package com.daruc.towerdefence.building.lasertower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class LaserTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return LaserTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, int mapX, int mapY) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapX, mapY);
        Building building = gameMap.getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS && building == null) {
            LaserTower laserTower = new LaserTower(new PointF(mapX + 0.5f, mapY + 0.5f));
            gameMap.setBuilding(mapX, mapY, laserTower);
            mapView.setGold(mapView.getGold() - LaserTower.COST);
        }
    }
}
