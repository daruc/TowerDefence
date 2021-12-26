package com.daruc.towerdefence.building.areadamagetower;

import android.graphics.PointF;
import android.hardware.Camera;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class AreaDamageTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return AreaDamageTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, int mapX, int mapY) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapX, mapY);
        Building building = gameMap.getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS && building == null) {

            AreaDamageTower areaDamageTower
                    = new AreaDamageTower(new PointF(mapX + 0.5f, mapY + 0.5f));

            gameMap.setBuilding(mapX, mapY, areaDamageTower);
            mapView.setGold(mapView.getGold() - AreaDamageTower.COST);
        }
    }
}
