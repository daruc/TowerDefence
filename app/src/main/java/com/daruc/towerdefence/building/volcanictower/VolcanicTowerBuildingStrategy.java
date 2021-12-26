package com.daruc.towerdefence.building.volcanictower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class VolcanicTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return VolcanicTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, int mapX, int mapY) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapX, mapY);
        Building building = gameMap.getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS
                && building == null &&
                gameMap.isNextToPath(mapX, mapY)) {

            VolcanicTower volcanicTower =
                    new VolcanicTower(new PointF(mapX + 0.5f, mapY + 0.5f));
            gameMap.setBuilding(mapX, mapY, volcanicTower);
            mapView.setGold(mapView.getGold() - VolcanicTower.COST);
        }
    }
}
