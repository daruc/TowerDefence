package com.daruc.towerdefence.building.roundtower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class RoundTowerBuildingStrategy implements BuildingStrategy {

    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return RoundTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, int mapX, int mapY) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapX, mapY);
        Building building = gameMap.getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS && building == null) {
            RoundTower roundTower = new RoundTower(new PointF(mapX + 0.5f, mapY + 0.5f));
            gameMap.setBuilding(mapX, mapY, roundTower);
            mapView.setGold(mapView.getGold() - RoundTower.COST);

            //PowerGenerator powerGenerator = findPowerGenerator(x, y);
            //roundTower.setPowerGenerator(powerGenerator);
        }
    }
}
